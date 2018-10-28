package util;

import java.util.Collection;
import java.util.Iterator;

import com.google.common.collect.Iterables;

public abstract class Concatenator<E> implements Collection<E> 
{
	Collection<? extends E>[] collections;
	@SafeVarargs
	public Concatenator(Collection<? extends E>... collections )
	{
		this.collections = collections;
	}
	
	@Override
	public Iterator<E> iterator()
	{
		return Iterables.concat(collections).iterator();
	
	}
	
	@Override
	public boolean remove(Object obj)
	{
		Iterator<E> iterator = iterator();
		boolean found = false;
		while(iterator.hasNext())
		{
			E next = iterator.next();
			if(next.equals(obj)) 
			{
				iterator.remove();
				found = true;
			}
		}
		return found;
	}
	
	@Override
	public boolean removeAll(Collection<?> objects)
	{
		Iterator<E> iterator = iterator();
		boolean found = false;
		while(iterator.hasNext())
		{
			E next = iterator.next();
			if(objects.contains(next)) 
			{
				iterator.remove();
				found = true;
			}
		}
		return found;
	}
	
	@Override
	public boolean contains(Object obj)
	{
		for(Collection<? extends E> c : collections)
		{
			if(c.contains(obj)) return true;
		}
		return false;
	}
	
	@Override
	public boolean containsAll(Collection<?> objects)
	{
		for(Object obj : objects)
		{
			for(Collection<? extends E> c : collections)
			{
				if(!c.contains(obj)) return false;
			}
		}
		return true;
	}
	
	@Override
	public void clear()
	{
		for(Collection<? extends E> c : collections)
		{
			c.clear();
		}
	}
	
	@Override
	public int size()
	{
		int size = 0;
		for(Collection<? extends E> c : collections)
		{
			size += c.size();
		}
		return size;
	}
	
	@Override
	public boolean isEmpty()
	{
		return size() == 0;
	}
	
	@Override
	public boolean retainAll(Collection<?> objects)
	{
		boolean changed = false;
		for(Collection<? extends E> c : collections)
		{
			if(c.retainAll(objects)) changed = true;
		}
		return changed;
	}
	
	@Override
	public Object[] toArray()
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public <T> T[] toArray(T[] arg)
	{
		throw new UnsupportedOperationException();
	}
	
	
}
