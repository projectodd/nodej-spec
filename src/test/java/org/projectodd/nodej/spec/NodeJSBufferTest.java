package org.projectodd.nodej.spec;

import java.io.File;

public class NodeJSBufferTest extends SpecTestHelper {
    
    public NodeJSBufferTest() {
        super("simple");
    }
    
    @Override
    public boolean shouldRun(File file) {
        return file.getName().equals("test-buffer.js");
    }
}
