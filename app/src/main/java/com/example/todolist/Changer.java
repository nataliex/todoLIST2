package com.example.todolist;
@FunctionalInterface

//Теперь еще передаем и тип создаваемого фрагмента
public interface Changer {
    void changeFragment(int i, int typeFragment);
}
