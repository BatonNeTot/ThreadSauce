package com.notjuststudio.threadsauce;

import com.sun.istack.internal.NotNull;

public interface DoFunction <T>{

    T doWith(final T value);

}
