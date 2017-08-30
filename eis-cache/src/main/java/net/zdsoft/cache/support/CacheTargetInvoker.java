package net.zdsoft.cache.support;

/**
 * @author shenke
 * @since 17-8-29下午10:30
 */
public interface CacheTargetInvoker {

    Object invoke() throws CacheTargetThrowableWrapper;

    class CacheTargetThrowableWrapper extends RuntimeException {
        private Throwable original;

        public CacheTargetThrowableWrapper(Throwable original) {
            this.original = original;
        }

        public Throwable getOriginal() {
            return original;
        }
    }
}
