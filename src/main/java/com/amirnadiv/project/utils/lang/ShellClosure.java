package com.amirnadiv.project.utils.common.lang;

import com.amirnadiv.project.utils.common.able.Processable;

public class ShellClosure implements Processable {

    private Processable processable;

    public ShellClosure() {
        this(new ProcessClosure());
    }

    public ShellClosure(Processable processable) {
        this.processable = processable;
    }

    @Override
    public void execute(Object...input) {
        for (Object cmd : input) {
            if (cmd.getClass().isArray()) {
                processable.execute(cmd);
                continue;
            }
            processable.execute(cmd2Shell(cmd.toString()));
        }
    }

    @Override
    public String getInfo() {
        return processable.getInfo();
    }

    @Override
    public void execute(String...input) {
        for (String cmd : input) {
            processable.execute(cmd2Shell(cmd));
        }
    }

    @Override
    public void execute(String input) {
        processable.execute(cmd2Shell(input));
    }

    private String[] cmd2Shell(String input) {
        return new String[] { "sh", "-c", input };
    }

}
