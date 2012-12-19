package org.projectodd.nodej.spec.simple;

import java.io.File;

import org.projectodd.nodej.spec.NodeJTestHelper;

public class ChdirTest extends NodeJTestHelper {
    
    public ChdirTest() {
        super("simple");
    }
    
    @Override
    public boolean shouldRun(File file) {
        return file.getName().equals("test-sys.js");
    }
}
