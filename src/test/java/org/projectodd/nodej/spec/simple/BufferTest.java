package org.projectodd.nodej.spec.simple;

import java.io.File;

import org.projectodd.nodej.spec.NodeJTestHelper;

public class BufferTest extends NodeJTestHelper {
    
    public BufferTest() {
        super("simple");
    }
    
    @Override
    public boolean shouldRun(File file) {
        return file.getName().equals("test-buffer.js");
    }
}
