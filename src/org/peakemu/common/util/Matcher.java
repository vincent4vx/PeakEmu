/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peakemu.common.util;

/**
 *
 * @author Vincent Quatrevieux <quatrevieux.vincent@gmail.com>
 */
public interface Matcher<T> {
    final static public Matcher ALLOW_ALL = new Matcher() {
        @Override
        public boolean match(Object obj) {
            return true;
        }
    };
    
    public boolean match(T obj);
}
