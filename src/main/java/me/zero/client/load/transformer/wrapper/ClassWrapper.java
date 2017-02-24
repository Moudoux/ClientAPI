package me.zero.client.load.transformer.wrapper;

import com.google.common.collect.Sets;
import javassist.*;
import me.zero.client.api.exception.ActionNotValidException;
import me.zero.client.api.util.logger.Level;
import me.zero.client.api.util.logger.Logger;
import me.zero.client.load.transformer.hook.ClassHook;
import me.zero.client.load.transformer.reference.ClassReference;
import me.zero.client.load.transformer.reference.FieldReference;

import java.util.Set;

import static me.zero.client.api.util.Messages.TRANSFORM_WRAPPER_ADD_METHOD;
import static me.zero.client.api.util.Messages.TRANSFORM_WRAPPER_COMPILE_METHOD;

/**
 * Basic class wrapper, used to attach interfaces.
 *
 * @since 1.0
 *
 * Created by Brady on 2/23/2017.
 */
public abstract class ClassWrapper {

    /**
     * Set of CtMethod representations of the Methods in the Wrapper Interface
     */
    private Set<CtMethod> methods = Sets.newHashSet();

    /**
     * The target class being "wrapped"
     */
    private CtClass target;

    /**
     * The Wrapper Interface
     */
    private Class<?> wrapper;

    public ClassWrapper(ClassReference target, Class<?> wrapper) {
        this.wrapper = wrapper;
        this.target = target.getCtClass();
        this.loadImplementations();
    }

    /**
     * Called to load all method implementations
     *
     * @since 1.0
     */
    protected abstract void loadImplementations();

    /**
     * Implements a return method from a FieldReference
     *
     * @since 1.0
     *
     * @param methodName The name of the method
     * @param returnType The return type of the method as a CtClass
     */
    protected void implement(String methodName, CtClass returnType, FieldReference reference) {
        this.implement(methodName, returnType, reference.createReturn());
    }

    /**
     * Implements a method from its method data
     *
     * @since 1.0
     *
     * @param methodName The name of the method
     * @param returnType The return type of the method as a CtClass
     * @param src The source code of the method
     */
    protected void implement(String methodName, CtClass returnType, String src) {
        this.implement(methodName, returnType, new CtClass[0], src);
    }

    /**
     * Implements a method from its method data
     *
     * @since 1.0
     *
     * @param methodName The name of the method
     * @param returnType The return type of the method as a CtClass
     * @param parameters An array of method parameters
     * @param src The source code of the method
     */
    protected void implement(String methodName, CtClass returnType, CtClass[] parameters, String src) {
        this.implement(methodName, returnType, parameters, new CtClass[0], src);
    }

    /**
     * Implements a method from its method data
     *
     * @since 1.0
     *
     * @param methodName The name of the method
     * @param returnType The return type of the method as a CtClass
     * @param parameters An array of method parameters
     * @param exceptions An array of exceptions being thrown
     * @param src The source code of the method
     */
    protected void implement(String methodName, CtClass returnType, CtClass[] parameters, CtClass[] exceptions, String src) {
        try {
            this.implement(CtNewMethod.make(returnType, methodName, parameters, exceptions, src, target));
        } catch (CannotCompileException e) {
            Logger.instance.logf(Level.SEVERE, TRANSFORM_WRAPPER_COMPILE_METHOD, methodName);
        }
    }

    /**
     * Implements a CtMethod
     *
     * @since 1.0
     *
     * @param method Method being implemented
     */
    protected void implement(CtMethod method) {
        if (!methods.contains(method))
            methods.add(method);
    }

    /**
     * @since 1.0
     *
     * @return Whether or not all methods have been implemented
     */
    private boolean isImplComplete() {
        // Basic check, change later
        return wrapper.getClass().getDeclaredMethods().length == methods.size();
    }

    /**
     * Creates a ClassHook from the Wrapper
     *
     * @since 1.0
     *
     * @return Created ClassHook
     */
    public ClassHook createClassHook() {
        if (isImplComplete())
            throw new ActionNotValidException("ClassHook cannot be created if implementation isn't complete!");

        return ctClass -> {
            ctClass.addInterface(target);
            methods.forEach(method -> {
                try {
                    ctClass.addMethod(method);
                } catch (CannotCompileException e) {
                    Logger.instance.logf(Level.SEVERE, TRANSFORM_WRAPPER_ADD_METHOD, method.getName());
                }
            });
        };
    }
}