package com.amirnadiv.project.utils.common.diagnostic;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;

import com.amirnadiv.project.utils.common.CollectionUtil;
import com.amirnadiv.project.utils.common.Emptys;
import com.amirnadiv.project.utils.common.ObjectUtil;
import com.amirnadiv.project.utils.common.StringUtil;

public final class Profiler {
    private static final ThreadLocal<Entry> entryStack = new ThreadLocal<Entry>();

    public static void start() {
        start((String) null);
    }

    public static void start(String message) {
        Entry currentEntry = getCurrentEntry();

        if (currentEntry != null) {
            currentEntry.enterSubEntry(message);
        } else {
            entryStack.set(new Entry(message, null, null));
        }
    }

    public static void start(Message message) {
        Entry currentEntry = getCurrentEntry();

        if (currentEntry != null) {
            currentEntry.enterSubEntry(message);
        } else {
            entryStack.set(new Entry(message, null, null));
        }
    }

    public static void reset() {
        entryStack.set(null);
    }

    public static void release() {
        Entry currentEntry = getCurrentEntry();

        if (currentEntry != null) {
            currentEntry.release();
        }
    }

    public static long getDuration() {
        Entry entry = entryStack.get();

        if (entry != null) {
            return entry.getDuration();
        }
        return -1;
    }

    public static String dump() {
        return dump("", "");
    }

    public static String dump(String prefix) {
        return dump(prefix, prefix);
    }

    public static String dump(String prefix1, String prefix2) {
        Entry entry = entryStack.get();

        if (entry != null) {
            return entry.toString(prefix1, prefix2);
        }
        return Emptys.EMPTY_STRING;
    }

    public static boolean isStart() {
        return entryStack.get() != null;
    }

    public static boolean isEmpty() {
        return entryStack.get() == null;
    }

    public static boolean isEnd() {
        Entry firstEntry = entryStack.get();

        if (firstEntry == null) {
            return false;
        }
        Entry currentEntry = getCurrentEntry();

        return (currentEntry == firstEntry) && firstEntry.isReleased();
    }

    public static String dumpIfFirst(String prefix1, String prefix2) {
        Entry firstEntry = entryStack.get();
        if (firstEntry == null) {
            return null;
        }
        Entry currentEntry = getCurrentEntry();
        if (currentEntry == firstEntry) {
            String result = dump(prefix1, prefix2);
            reset();
            return result;

        }
        return null;
    }

    public static String dumpIfFirst(String prefix) {
        return dumpIfFirst(prefix, prefix);
    }

    public static String dumpIfFirst() {
        return dumpIfFirst("", "");
    }

    public static Entry getEntry() {
        return entryStack.get();
    }

    private static Entry getCurrentEntry() {
        Entry subEntry = entryStack.get();
        Entry entry = null;

        if (subEntry != null) {
            do {
                entry = subEntry;
                subEntry = entry.getUnreleasedEntry();
            } while (subEntry != null);
        }

        return entry;
    }

    public static final class Entry {
        private final List<Entry> subEntries = CollectionUtil.createArrayList(4);
        private final Object message;
        private final Entry parentEntry;
        private final Entry firstEntry;
        private final long baseTime;
        private final long startTime;
        private long endTime;


        private Entry(Object message, Entry parentEntry, Entry firstEntry) {
            this.message = message;
            this.startTime = System.currentTimeMillis();
            this.parentEntry = parentEntry;
            this.firstEntry = (Entry) ObjectUtil.defaultIfNull(firstEntry, this);
            this.baseTime = (firstEntry == null) ? 0 : firstEntry.startTime;
        }


        public String getMessage() {
            String messageString = null;

            if (String.class.isInstance(message)) {
                messageString = (String) message;
            } else if (Message.class.isInstance(message)) {
                Message messageObject = (Message) message;
                MessageLevel level = MessageLevel.BRIEF_MESSAGE;

                if (isReleased()) {
                    level = messageObject.getMessageLevel(this);
                }

                if (level == MessageLevel.DETAILED_MESSAGE) {
                    messageString = messageObject.getDetailedMessage();
                } else {
                    messageString = messageObject.getBriefMessage();
                }
            }

            return StringUtil.defaultIfEmpty(messageString, null);
        }


        public long getStartTime() {
            return (baseTime > 0) ? (startTime - baseTime) : 0;
        }


        public long getEndTime() {
            if (endTime < baseTime) {
                return -1;
            }
            return endTime - baseTime;
        }


        public long getDuration() {
            if (endTime < startTime) {
                return -1;
            }
            return endTime - startTime;
        }


        public long getDurationOfSelf() {
            long duration = getDuration();

            if (duration < 0) {
                return -1;
            }
            if (subEntries.isEmpty()) {
                return duration;
            }

            for (int i = 0; i < subEntries.size(); i++) {
                Entry subEntry = subEntries.get(i);

                duration -= subEntry.getDuration();
            }

            if (duration < 0) {
                return -1;
            }
            return duration;

        }


        public double getPecentage() {
            double parentDuration = 0;
            double duration = getDuration();

            if ((parentEntry != null) && parentEntry.isReleased()) {
                parentDuration = parentEntry.getDuration();
            }

            if ((duration > 0) && (parentDuration > 0)) {
                return duration / parentDuration;
            }
            return 0;
        }


        public double getPecentageOfAll() {
            double firstDuration = 0;
            double duration = getDuration();

            if ((firstEntry != null) && firstEntry.isReleased()) {
                firstDuration = firstEntry.getDuration();
            }

            if ((duration > 0) && (firstDuration > 0)) {
                return duration / firstDuration;
            }
            return 0;
        }


        public List<Entry> getSubEntries() {
            return Collections.unmodifiableList(subEntries);
        }


        private void release() {
            endTime = System.currentTimeMillis();
        }


        private boolean isReleased() {
            return endTime > 0;
        }


        private void enterSubEntry(Object message) {
            Entry subEntry = new Entry(message, this, firstEntry);

            subEntries.add(subEntry);
        }


        private Entry getUnreleasedEntry() {
            Entry subEntry = null;

            if (!subEntries.isEmpty()) {
                subEntry = subEntries.get(subEntries.size() - 1);

                if (subEntry.isReleased()) {
                    subEntry = null;
                }
            }

            return subEntry;
        }


        public String toString() {
            return toString("", "");
        }


        private String toString(String prefix1, String prefix2) {
            StringBuilder builder = new StringBuilder();

            toString(builder, prefix1, prefix2);

            return builder.toString();
        }


        private void toString(StringBuilder builder, String prefix1, String prefix2) {
            builder.append(prefix1);

            String message = getMessage();
            long startTime = getStartTime();
            long duration = getDuration();
            long durationOfSelf = getDurationOfSelf();
            double selfPercent = duration == 0 ? 0 : (double) durationOfSelf / duration;
            double percent = getPecentage();
            double percentOfAll = getPecentageOfAll();

            Object[] params = new Object[] { message, // {0} - entry信息
                    Long.valueOf(startTime), // {1} - 起始时间
                    Long.valueOf(duration), // {2} - 持续总时间
                    Long.valueOf(durationOfSelf), // {3} - 自身消耗的时间
                    Double.valueOf(selfPercent), // {4} -
                    // 自身消耗在持续时间中所占的比例
                    Double.valueOf(percent), // {5} - 在父entry中所占的时间比例
                    Double.valueOf(percentOfAll) // {6} - 在总时间中所占的比例
                    };

            StringBuilder pattern = new StringBuilder("startTime: {1,number}ms ");

            if (isReleased()) {
                pattern.append(", duration: [{2,number}ms");

                if ((durationOfSelf > 0) && (durationOfSelf != duration)) {
                    pattern.append(",durationOfSelf: ({3,number}ms");
                    pattern.append(",selfPercent: {4,number,##.##%})");
                }

                if (percent > 0) {
                    pattern.append(", percent: {5,number,##.##%}");
                }

                if (percentOfAll > 0) {
                    pattern.append(", percentOfAll: {6,number,##.##%}");
                }

                pattern.append("]");
            } else {
                pattern.append("[UNRELEASED]");
            }

            if (message != null) {
                pattern.append(" - {0}");
            }

            builder.append(MessageFormat.format(pattern.toString(), params));

            for (int i = 0; i < subEntries.size(); i++) {
                Entry subEntry = subEntries.get(i);

                builder.append('\n');

                if (i == (subEntries.size() - 1)) {
                    subEntry.toString(builder, prefix2 + "`---", prefix2 + "    "); // 最后一项
                } else if (i == 0) {
                    subEntry.toString(builder, prefix2 + "+---", prefix2 + "|   "); // 第一项
                } else {
                    subEntry.toString(builder, prefix2 + " ---", prefix2 + "|   "); // 中间项
                }
            }
            // reset();
        }
    }

    public static enum MessageLevel {

        NO_MESSAGE, BRIEF_MESSAGE, DETAILED_MESSAGE;

    }

    public interface Message {
        MessageLevel getMessageLevel(Entry entry);

        String getBriefMessage();

        String getDetailedMessage();
    }

}
