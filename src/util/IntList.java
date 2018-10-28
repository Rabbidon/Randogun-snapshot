package util;

import java.util.Arrays;

public class IntList
{
	private int[] data;
	private int size;
	
	public IntList(int capacity)
	{
		data = new int[capacity];
		size = 0;
	}
	
	public IntList()
	{
		this(8);
	}
	
	public int get(int index)
	{
		return data[index];
	}
	
	public void add(int a)
	{
		if(size == data.length)
		{
			setCapacity(data.length * 2);
		}
		data[size++] = a;
	}
	
	public void set(int index, int i)
	{
		data[index] = i;
	}
	
	
	public void setCapacity(int newCapacity)
	{
		data = Arrays.copyOf(data, newCapacity);
	}

	public void clear()
	{
		size = 0;
	}
	
	public int size()
	{
		return size;
	}
	
}
