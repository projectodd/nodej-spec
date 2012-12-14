package org.projectodd.nodej.spec;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;

import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.projectodd.nodej.Node;

public class NodeJSpecRunner extends Runner {

    private Class<?> testClass;
    private Collection<File> files;
    private Object testInstance;
    private Method shouldRun;

    static {
        String testsDir = System.getProperty("user.dir") + "/src/main/javascript/node/lib";
        String libDir   = System.getProperty("user.dir") + "/src/test/resources/suite";
        String subDir   = libDir + "/simple";
        System.setProperty("dynjs.require.path", testsDir + ":" + subDir + ":" + libDir);
        System.setProperty("java.library.path", System.getProperty("user.dir") + "/lib");
    }

    @SuppressWarnings("unchecked")
    public NodeJSpecRunner(Class<?> testClass) {
        this.testClass = testClass;
        try {
            Constructor<?> constructor = testClass.getConstructor((Class<?>[]) null);
            testInstance = constructor.newInstance();
            Class<?>[] args = new Class<?>[1];
            args[0] = File.class;
            this.shouldRun = testClass.getMethod("shouldRun", args);
            Method getFiles = testClass.getMethod("files", (Class<?>[]) null);
            this.files = (Collection<File>) getFiles.invoke(testInstance, (Object[]) null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Description getDescription() {
        final Description description = Description.createSuiteDescription(testClass);
        for (File file : files) {
            if (this.shouldRun(file)) {
                Description child = Description.createTestDescription(testClass, file.getName());
                description.addChild(child);
            }
        }
        return description;
    }

    @Override
    public void run(RunNotifier notifier) {
        for (File file : this.files) {
            if (this.shouldRun(file)) {
                Description description = Description.createTestDescription(testClass, file.getName());
                try {
                    notifier.fireTestStarted(description);
                    Node node = new Node();
                    node.start();
                    FileInputStream testFile = new FileInputStream(file);
                    node.execute(testFile, file.getName());
                } catch (Throwable e) {
                    notifier.fireTestFailure(new Failure(description, e));
                } finally {
                    notifier.fireTestFinished(description);
                }
            }
        }
    }

    private boolean shouldRun(File file) {
        try {
            return (boolean) shouldRun.invoke(testInstance, file);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            System.err.println("Cannot determine if we should run file " + file.getName() + ". Skipping.");
            e.printStackTrace();
        }
        return false;
    }

}
