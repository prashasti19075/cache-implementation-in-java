import java.util.Scanner;
public class Cache_Project {
	// We have a 16 bit system 
	public static int fasize=0;
	public static block[] main_memory;
	public static class node
	{
		int priority;
		String value;
		int setsize;
		node(String s)
		{
			priority=0;
			value=s;
		}
		node(String s,int e)
		{
			setsize=e;
			priority=0;
			value=s;
		}
	}
	public static class adress_fa
	{
		String tag; //tag is a number_of_tag_bits long String 
		String offset;
		adress_fa(String t,String o)
		{
			tag=t;
			offset=o;			
		}
	}

	public static class block 
	{
		int size;
		int[] contents;
		block(int s)
		{
			contents=new int[s];
			size=s;
		}
	}
	public static int read(adress_fa entry,int cl,node[] tag_array,block[] data_array)
	{
		block required=null;
		
		for(int i=0;i<fasize;i++)
		{
			//System.out.println(" tag is : " + tag_array[i]);
			if(entry.tag.contentEquals(tag_array[i].value))
			{
				required=data_array[i];
				tag_array[i].priority++;
				break;
			}
		}
		//cache hit
		if(required!=null)
		{
			String offset=entry.offset;
			int decimal=Integer.parseInt(offset,2); 
			int data=required.contents[decimal];
			return data;
		}
		else
		{
			System.out.println(" Cache miss! block loaded from main memory");
			if(fasize<cl)
			{
			insert(tag_array,entry,main_memory,data_array);
			required=data_array[fasize];
			String offset=entry.offset;
			int decimal=Integer.parseInt(offset,2); 
			int data=required.contents[decimal];
			tag_array[fasize].priority++;
			fasize++;
			return data;
			}
			else
			{
				int index=replace(cl,tag_array);
				tag_array[index]=new node(entry.tag);
				data_array[index]=main_memory[Integer.parseInt(entry.tag,2)];
				required=data_array[index];
				String offset=entry.offset;
				int decimal=Integer.parseInt(offset,2); 
				int data=required.contents[decimal];
				tag_array[index].priority++;
				return data;
			}
			
		}
	}
	public static void print_cache(int cache_size,node[] tag_array,block[] data_array,int offset_size)
	{
		System.out.println(" Cache Display :");
		for(int i=0;i<cache_size;i++)
		{
			if(tag_array[i].value.contentEquals(""))
			{
				System.out.print("-");
			}
			else
			{
				System.out.print(tag_array[i].value+ " -> ");
				for(int j=0;j<offset_size;j++)
				{
					System.out.print(data_array[i].contents[j]+" ");
				}
			}
			System.out.println();
		}
	}
	public static void print_cache_dm(int cache_size,String[] tag_array,block[] data_array,int offset_size)
	{
		System.out.println(" Cache Display :");
		for(int i=0;i<cache_size;i++)
		{
			if(tag_array[i].contentEquals(""))
			{
				System.out.print("-");
			}
			else
			{
				System.out.print(tag_array[i]+ " -> ");
				for(int j=0;j<offset_size;j++)
				{
					System.out.print(data_array[i].contents[j]+" ");
				}
			}
			System.out.println();
		}
	}
	public static void insert(node[] tag_array,adress_fa entry,block[ ] main_memory,block[] data_array)
	{
		tag_array[fasize]=new node(entry.tag);
		data_array[fasize]=main_memory[Integer.parseInt(entry.tag,2)];
	}
	public static int replace(int cl,node[] tag_array)
	{
		//it will place the least used block to 
		System.out.println(" Cache full!,preparing to evict");
		int min_priority_index=-1;
		int min_value=2147483647;
		for(int i=0;i<cl;i++)
		{
			if(min_value>tag_array[i].priority)
			{
				min_value=tag_array[i].priority;
				min_priority_index=i;
			}
		}
		System.out.println("Entry evicted = "+ tag_array[min_priority_index].value);
		return min_priority_index;
	}
	public static void write(adress_fa entry,int cl,node [] tag_array,block[] data_array,int data)
	{
		block required=null;
		for(int i=0;i<fasize;i++)
		{
			if(entry.tag.contentEquals(tag_array[i].value))
			{
				required=data_array[i];
				tag_array[i].priority++;
				break;
			}
		}
		//cache hit
		if(required!=null)
		{
			String offset=entry.offset;
			int decimal=Integer.parseInt(offset,2); 
			required.contents[decimal]=data;
		}
		//cache miss
		else
		{
			System.out.println(" Cache Miss! block loaded from main memory ");
			if(fasize<cl)
			{
				insert(tag_array,entry,main_memory,data_array);
				required=data_array[fasize];
				String offset=entry.offset;
				int decimal=Integer.parseInt(offset,2); 
				required.contents[decimal]=data;
				tag_array[fasize].priority++;
				fasize++;
			}
			else
			{
				int index=replace(cl,tag_array);
				tag_array[index]=new node(entry.tag);
				data_array[index]=main_memory[Integer.parseInt(entry.tag,2)];
				required=data_array[index];
				String offset=entry.offset;
				int decimal=Integer.parseInt(offset,2); 
				required.contents[decimal]=data;
				tag_array[index].priority++;
			}
			}
		
	}
	public static void direct_cache(Scanner in,int n)
	{		
		System.out.println(" Enter number of cache lines ( number of blocks) : ");
		int cl=in.nextInt();	// number of blocks in the cache
		System.out.println(" Enter block size (bytes) :");
		int b=in.nextInt(); 	// the size of block in a cache
		double offset= log_2(b);
		// System.out.println(" Number of bits in offset are : log2(b) "+ offset);
		double index=log_2(cl);
		double number_of_tag_bits= n-offset-index;
		System.out.println(" Enter the number of operations need to be performed: ");
		int operations=in.nextInt();
									// the number of operations to be performed by the user 
		String[] tag_array= new String[cl];
		block[] data_array=new block[cl];
		for(int i=0;i<(int)Math.pow(2,n);i++)
		{
			main_memory[i]=new block(b);
		}
		for(int i=0;i<cl;i++)
		{
			tag_array[i]="";
		}
		for(int i=0;i<cl;i++)
		{
			data_array[i]=new block(b);
		}
		for(int i=0;i<operations;i++)
		{
			String instruction=in.next();
			char c=instruction.charAt(0);
			if(c=='w')
			{
				String  entry=in.next();
				int data=in.nextInt();
				String tag="";
				String ind="";
				String offset_value="";
				for(int j=0;j<number_of_tag_bits;j++)
				{
					tag+=entry.charAt(j);
				}
				for(int j=(int)number_of_tag_bits;j<number_of_tag_bits+index;j++)
				{
					ind+=entry.charAt(j);
				}
				for(int j=(int)number_of_tag_bits+(int)index;j<n;j++)
				{
					offset_value+=entry.charAt(j);
				}
				int index_dm=Integer.parseInt(ind,2);
				if(tag_array[index_dm].contentEquals(tag))
				{
					data_array[index_dm].contents[Integer.parseInt(offset_value,2)]=data;
				}
				else
				{
					System.out.println(" Cache miss!, block loaded from main memory");
					tag_array[index_dm]=tag;
					data_array[index_dm]=main_memory[Integer.parseInt(tag+ind,2)];
					data_array[index_dm].contents[Integer.parseInt(offset_value,2)]=data;
				}
				
			}
			if(c=='r')
			{
				String  entry=in.next();
				String tag="";
				String ind="";
				String offset_value="";
				for(int j=0;j<number_of_tag_bits;j++)
				{
					tag+=entry.charAt(j);
				}
				for(int j=(int)number_of_tag_bits;j<number_of_tag_bits+index;j++)
				{
					ind+=entry.charAt(j);
				}
				for(int j=(int)number_of_tag_bits+(int)index;j<n;j++)
				{
					offset_value+=entry.charAt(j);
				}
				int index_dm=Integer.parseInt(ind,2);
				if(tag_array[index_dm].contentEquals(tag))
				{
					System.out.println(" contents of entry "+ tag+ind+ "are:"+ data_array[index_dm].contents[Integer.parseInt(offset_value,2)]);
				}
				else
				{
					System.out.println(" Cache miss!, block loaded from main memory");
					tag_array[index_dm]=tag;
					data_array[index_dm]=main_memory[Integer.parseInt(tag+ind,2)];
					System.out.println(" contents of entry "+ tag+ind+ "are:"+ data_array[index_dm].contents[Integer.parseInt(offset_value,2)]);
				}
			}
			print_cache_dm(cl,tag_array,data_array,b);
			
		}
		
	}
	public static double log_2(long a)
	{
		double offset=java.lang.Math.log(a);
		double log2= java.lang.Math.log(2);
		return offset/log2;
	}
	public static void fully_associative_cache(Scanner in,int n)
	{
		System.out.println(" Enter number of cache lines ( number of blocks) : ");
		int cl=in.nextInt();	// number of blocks in the cache
		System.out.println(" Enter block size (bytes) :");
		int b=in.nextInt(); 	// the size of block in a cache
		double offset= log_2(b);
		// System.out.println(" Number of bits in offset are : log2(b) "+ offset);
		double number_of_tag_bits= n-offset;
		System.out.println(" Enter the number of operations need to be performed: ");
		int operations=in.nextInt();
									// the number of operations to be performed by the user 
		node[] tag_array= new node[cl];
		block[] data_array=new block[cl];
		for(int i=0;i<cl;i++)
		{
			tag_array[i]=new node("");
		}
		for(int i=0;i<(int)Math.pow(2,n);i++)
		{
			main_memory[i]=new block(b);
		}
		for(int i=0;i<cl;i++)
		{
			data_array[i]=new block(b);
		}
		for(int i=0;i<operations;i++)
		{
			String instruction=in.next();
			char c=instruction.charAt(0);
			if(c=='w')
			{
				String entry=in.next();
				int data=in.nextInt();
				String tag="";
				String offset_value="";
				for(int j=0;j<number_of_tag_bits;j++)
				{
					tag+=entry.charAt(j);
				}
				for(int j=(int)number_of_tag_bits;j<n;j++)
				{
					offset_value+=entry.charAt(j);
				}
				adress_fa query=new adress_fa(tag,offset_value);
				write(query,cl,tag_array,data_array,data);
			}
			if(c=='r')
			{
				String  entry=in.next();
				 String tag="";
				 String offset_value="";
				for(int j=0;j<number_of_tag_bits;j++)
				{
					tag+=entry.charAt(j);
				}
				for(int j=(int)number_of_tag_bits;j<n;j++)
				{
					offset_value+=entry.charAt(j);
				}
				adress_fa query=new adress_fa(tag,offset_value);
				System.out.println("Contents of " + entry+ " are : " + read(query,cl,tag_array,data_array));
			}
			print_cache(cl,tag_array,data_array,b);
		}
	}
	public static void set_associative_cache(Scanner in,int n)
	{
		System.out.println(" Enter number of cache lines (number of blocks) : ");
		int cl=in.nextInt();	// number of blocks in the cache
		System.out.println(" Enter block size (bytes) :");
		int b=in.nextInt(); 	// the size of block in a cache
		System.out.println("Enter number of blocks in a set : ");
		int set=in.nextInt();
		double offset= log_2(b);
		double no_set_index=log_2(cl/set);
		// System.out.println(" Number of bits in offset are : log2(b) "+ offset);
		double number_of_tag_bits= n-offset-no_set_index;
		node[] tag_array=new node[cl];
		block[] data_array=new block[cl];
		for(int i=0;i<cl;i++)
		{
			tag_array[i]=new node("",0);
			data_array[i]=new block(b);
		}
		for(int i=0;i<Math.pow(2, n);i++)
		{
			main_memory[i]=new block(b);
		}
		System.out.println(" Enter the number of operations need to be performed: ");
		int operations=in.nextInt();
		for(int i=0;i<operations;i++)
		{
			String instruction=in.next();
			char c=instruction.charAt(0);
			if(c=='w')
			{
				String entry=in.next();
				int data=in.nextInt();
				String tag="";
				String offset_value="";
				String index_value="";
				for(int j=0;j<number_of_tag_bits;j++)
				{
					tag+=entry.charAt(j);
				}
				for(int j=(int)number_of_tag_bits;j<number_of_tag_bits+no_set_index;j++)
				{
					index_value+=entry.charAt(j);
				}
				for(int j=(int)number_of_tag_bits+(int)no_set_index;j<n;j++)
				{
					offset_value+=entry.charAt(j);
				}
				//lookup 
				int index_v=Integer.parseInt(index_value,2);
				int found=0;
				for(int j=index_v*set;j<index_v*set+set;j++)
				{
					if(tag_array[j].value.equals(tag))
					{
						data_array[j].contents[Integer.parseInt(offset_value,2)]=data;
						found=1;
						tag_array[j].priority++;
						break;
					}
				}
				if(found==0)
				{
					System.out.println(" Cache miss! loading block from memory");
					int set_size=tag_array[Integer.parseInt(index_value,2)*set].setsize;
					//System.out.println(" set size is "+ set_size);
					if(set_size<set)
					{
						// insert 
						tag_array[index_v*set+set_size].value=tag;
						data_array[index_v*set+set_size]=main_memory[Integer.parseInt((tag+index_value),2)];
						data_array[index_v*set+set_size].contents[Integer.parseInt(offset_value,2)]=data;
						tag_array[index_v*set].setsize++;
						tag_array[index_v*set+set_size].priority++;
					}
					else
					{	// replace- evict- insert
						//replace 
						System.out.println(" Set filled! preparing to evict");
						int min_priority_index=-1;
						int min_value=2147483647;
						for(int j=index_v*set;j<index_v*set+set;j++)
						{
							if(min_value>tag_array[j].priority)
							{
								min_priority_index=j;
								min_value=tag_array[j].priority;
							}
						}
						System.out.println(" Entry evicted is "+ tag_array[min_priority_index].value);
						tag_array[min_priority_index].value=tag;
						data_array[min_priority_index]=main_memory[Integer.parseInt((tag+index_value),2)];
						data_array[min_priority_index].contents[Integer.parseInt(offset_value,2)]=data;
						tag_array[min_priority_index].priority=1;
					}
				}
			}
			else if(c=='r')
			{
				String entry=in.next();
				String tag="";
				String offset_value="";
				String index_value="";
				for(int j=0;j<number_of_tag_bits;j++)
				{
					tag+=entry.charAt(j);
				}
				for(int j=(int)number_of_tag_bits;j<number_of_tag_bits+no_set_index;j++)
				{
					index_value+=entry.charAt(j);
				}
				for(int j=(int)number_of_tag_bits+(int)no_set_index;j<n;j++)
				{
					offset_value+=entry.charAt(j);
				}
				//System.out.println(" "+ tag + " "+ index_value+ " "+ offset_value);
				int index_v=Integer.parseInt(index_value,2);
				int found=0;
				for(int j=index_v*set;j<index_v*set+set;j++)
				{
					if(tag_array[j].value.equals(tag))
					{
						System.out.println(" Contents of " + tag+index_value+ " is " + data_array[j].contents[Integer.parseInt(offset_value,2)]);
						found=1;
						tag_array[j].priority++;
						break;
					}
				}
				if(found==0)
				{
					System.out.println(" Cache miss! loading block from memory");
					int set_size=tag_array[index_v*set].setsize;
					//System.out.println(" set size "+ set_size);
					if(set_size<set)
					{
						// insert 
						tag_array[index_v*set+set_size].value=tag;
						data_array[index_v*set+set_size]=main_memory[Integer.parseInt((tag+index_value),2)];
						System.out.println(" Contents of " + tag+index_value+ " is " + data_array[index_v*set+set_size].contents[Integer.parseInt(offset_value,2)]);
						tag_array[index_v*set].setsize++;
						tag_array[index_v*set+set_size].priority++;
					}
					else
					{
						// replace- evict- insert
						// replace- evict- insert
						System.out.println(" Set filled! preparing to evict");
						//replace 
						int min_priority_index=-1;
						int min_value=2147483647;
						for(int j=index_v*set;j<index_v*set+set;j++)
						{
							if(min_value>tag_array[j].priority)
							{
								min_priority_index=j;
								min_value=tag_array[j].priority;
							}
						}

						System.out.println(" Entry evicted is "+ tag_array[min_priority_index].value);
						tag_array[min_priority_index].value=tag;
						data_array[min_priority_index]=main_memory[Integer.parseInt((tag+index_value),2)];
						System.out.println(" Contents of " + tag+index_value+ " is" +data_array[min_priority_index].contents[Integer.parseInt(offset_value,2)]);
						tag_array[min_priority_index].priority=1;
						
					}
				}
			}
			print_cache(cl,tag_array,data_array,b);
		}
	}
	public static void main(String[] args)
	{
		// TODO Auto-generated method stub
			Scanner in=new Scanner(System.in);
			System.out.println(" Enter n for n bit memory system :" );
			int memory_size=in.nextInt();
			main_memory=new block[(int)Math.pow(2,memory_size)];
			String waste=in.nextLine();
			System.out.println(" Enter the cache type : ");
			String cache_type=in.nextLine();
			//3 inputs : "direct mapping ", " associative memory ", " set associative" 
			
			if(cache_type.contentEquals("direct mapping"))
			{
				direct_cache(in,memory_size);
			}
			else if(cache_type.contentEquals("associative memory"))
			{
				fully_associative_cache(in,memory_size);
			}
			else if(cache_type.contentEquals("set associative"))
			{
				set_associative_cache(in,memory_size);
			}
			else
			{
				System.out.println(" Please enter from the three cache types :  ");
				System.out.println(" 1. direct mapping" );
				System.out.println(" 2. associative memory");
				System.out.println(" 3. set associative");
			}
	
	}

}
