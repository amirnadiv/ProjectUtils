
package com.amirnadiv.project.utils.common.able;

public interface Processable extends Closure {

    String getInfo();

    public void execute(String... input);

    public void execute(String input);

}
