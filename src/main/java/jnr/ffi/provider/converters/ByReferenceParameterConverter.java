package jnr.ffi.provider.converters;

import jnr.ffi.Memory;
import jnr.ffi.Pointer;
import jnr.ffi.byref.ByReference;
import jnr.ffi.mapper.ToNativeContext;
import jnr.ffi.mapper.ToNativeConverter;
import jnr.ffi.provider.ParameterFlags;
import jnr.ffi.provider.jffi.NativeRuntime;

/**
 *
 */
@ToNativeConverter.NoContext
@ToNativeConverter.Cacheable
public final class ByReferenceParameterConverter implements ToNativeConverter<ByReference, Pointer>, ToNativeConverter.PostInvocation<ByReference, Pointer> {
    private final jnr.ffi.Runtime runtime;
    private final int flags;

    public ByReferenceParameterConverter(jnr.ffi.Runtime runtime, int flags) {
        this.runtime = runtime;
        this.flags = flags;
    }

    public void postInvoke(ByReference byReference, Pointer pointer, ToNativeContext context) {
        if (ParameterFlags.isOut(flags)) {
            byReference.unmarshal(pointer, 0);
        }
    }

    public Pointer toNative(ByReference value, ToNativeContext context) {
        Pointer memory =  Memory.allocate(runtime, value.nativeSize(runtime));
        if (ParameterFlags.isIn(flags)) {
            value.marshal(memory, 0);
        }
        return memory;
    }

    public Class<Pointer> nativeType() {
        return Pointer.class;
    }
}
