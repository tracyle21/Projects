package com.example.teambeans;

import android.text.method.PasswordTransformationMethod;
import android.view.View;

/**
 * AsteriskPasswordTransformationMethod transform a charsquence into '*'
 *
 * @author Renee
 */
public class AsteriskPasswordTransformationMethod extends PasswordTransformationMethod {
    /**
     * Returns a CharSequence that is a transformation of the source text -- for example, replacing each character with a dot in a password field. Beware that the returned text must be exactly the same length as the source text, and that if the source text is Editable, the returned text must mirror it dynamically instead of doing a one-time copy. The method should not return null unless source is null.
     *
     * @param source the source text
     * @param view   the text's view
     * @return transformation of the source text
     */
    @Override
    public CharSequence getTransformation(CharSequence source, View view) {
        return new PasswordCharSequence(source);
    }

    /**
     * PasswordCharSequence contains method that will be used in AsteriskPasswordTransformationMethod
     */
    private class PasswordCharSequence implements CharSequence {
        private CharSequence mSource;

        /**
         * Constructor of PasswordCharsequence. Setting the source text.
         * @param source the source text
         */
        public PasswordCharSequence(CharSequence source) {
            mSource = source; // Store char sequence
        }

        /**
         * Return '*' for the character at index
         * @param index index that want to be transformed
         * @return character '*'
         */
        public char charAt(int index) {
            return '*';
        }

        /**
         * Return the length of source text
         * @return the length of source text
         */
        public int length() {
            return mSource.length(); // Return default
        }

        /**
         * Returns a CharSequence. CharSequence that is a subsequence of this sequence.
         * @param start This is the index from where the subsequence starts, it is inclusive.
         * @param end This is the index where the subsequence ends, it is exclusive.
         * @return It returns the specified subsequence in range [start, end].
         */
        public CharSequence subSequence(int start, int end) {
            return mSource.subSequence(start, end); // Return default
        }
    }
};