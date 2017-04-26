package com.sunesoft.seera.fr.matcher;

import java.io.Serializable;

/**
 * Created by zhouz on 2016/5/17.
 */
public abstract class AbstractMatcher<T> implements Matcher<T> {
    public AbstractMatcher() {
    }

    public Matcher<T> and(Matcher<? super T> other) {
        return new AbstractMatcher.AndMatcher(this, other);
    }

    public Matcher<T> or(Matcher<? super T> other) {
        return new AbstractMatcher.OrMatcher(this, other);
    }

    private static class OrMatcher<T> extends AbstractMatcher<T> implements Serializable {
        private final  Matcher<? super T> a;
        private final  Matcher<? super T> b;
        private static final long serialVersionUID = 0L;

        public OrMatcher( Matcher<? super T> a,  Matcher<? super T> b) {
            this.a = a;
            this.b = b;
        }

        public boolean matches(T t) {
            return this.a.matches(t) || this.b.matches(t);
        }

        public boolean equals(Object other) {
            return other instanceof AbstractMatcher.OrMatcher && ((AbstractMatcher.OrMatcher)other).a.equals(this.a) && ((AbstractMatcher.OrMatcher)other).b.equals(this.b);
        }

        public int hashCode() {
            return 37 * (this.a.hashCode() ^ this.b.hashCode());
        }

        public String toString() {
            return "or(" + this.a + ", " + this.b + ")";
        }
    }

    private static class AndMatcher<T> extends AbstractMatcher<T> implements Serializable {
        private final  Matcher<? super T> a;
        private final  Matcher<? super T> b;
        private static final long serialVersionUID = 0L;

        public AndMatcher( Matcher<? super T> a, Matcher<? super T> b) {
            this.a = a;
            this.b = b;
        }

        public boolean matches(T t) {
            return this.a.matches(t) && this.b.matches(t);
        }

        public boolean equals(Object other) {
            return other instanceof AbstractMatcher.AndMatcher && ((AbstractMatcher.AndMatcher)other).a.equals(this.a) && ((AbstractMatcher.AndMatcher)other).b.equals(this.b);
        }

        public int hashCode() {
            return 41 * (this.a.hashCode() ^ this.b.hashCode());
        }

        public String toString() {
            return "and(" + this.a + ", " + this.b + ")";
        }
    }
}
