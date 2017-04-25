package entities;

/**
 * Add class description
 */
public enum Alphabet implements Operation {
    ZERO {
        @Override
        public Alphabet and(Alphabet alphabet) {
            return ZERO;
        }

        @Override
        public Alphabet or(Alphabet alphabet) {
            return alphabet;
        }

        @Override
        public Alphabet not() {
            return ONE;
        }
    },

    ONE {
        @Override
        public Alphabet and(Alphabet alphabet) {
            if (alphabet == X) {
                return X;
            }
            return alphabet;
        }

        @Override
        public Alphabet or(Alphabet alphabet) {
            return ONE;
        }

        @Override
        public Alphabet not() {
            return ZERO;
        }
    },

    D {
        @Override
        public Alphabet and(Alphabet alphabet) {
            if (alphabet == ZERO || alphabet == NOT_D) {
                return ZERO;
            } else if (alphabet == X) {
                return X;
            }
            return D;
        }

        @Override
        public Alphabet or(Alphabet alphabet) {
            if (alphabet == ONE || alphabet == NOT_D) {
                return ONE;
            } else if (alphabet == X) {
                return X;
            }
            return D;
        }

        @Override
        public Alphabet not() {
            return NOT_D;
        }
    },

    NOT_D {
        @Override
        public Alphabet and(Alphabet alphabet) {
            if (alphabet == ZERO || alphabet == D) {
                return ZERO;
            } else if (alphabet == X) {
                return X;
            }
            return NOT_D;
        }

        @Override
        public Alphabet or(Alphabet alphabet) {
            if (alphabet == ONE || alphabet == D) {
                return ONE;
            } else if (alphabet == X) {
                return X;
            }
            return NOT_D;
        }

        @Override
        public Alphabet not() {
            return D;
        }
    },

    X {
        @Override
        public Alphabet and(Alphabet alphabet) {
            return alphabet == ZERO ? ZERO : X;
        }

        @Override
        public Alphabet or(Alphabet alphabet) {
            return alphabet == ONE ? ONE : X;
        }

        @Override
        public Alphabet not() {
            return X;
        }
    }
}
