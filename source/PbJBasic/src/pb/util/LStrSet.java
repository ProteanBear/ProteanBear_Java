/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pb.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

/**
 * 所有值均为小写字母字符串的 {@link java.util.HashSet}
 */
@SuppressWarnings("serial")
public class LStrSet extends HashSet<String>
{

    @Override
    public boolean add(String value)
    {
        return super.add(value.toLowerCase());
    }

    @Override
    public boolean remove(Object key)
    {
        key=key.toString().toLowerCase();
        return super.remove(key);
    }

    @Override
    public boolean contains(Object key)
    {
        key=key.toString().toLowerCase();
        return super.contains(key);
    }

    @Override
    public boolean retainAll(Collection<?> c)
    {
        ArrayList<String> list=new ArrayList<>(c.size());
        for(Object o : c)
        {
            list.add(o.toString().toLowerCase());
        }

        return super.retainAll(list);
    }

}
