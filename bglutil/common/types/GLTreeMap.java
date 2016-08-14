package bglutil.common.types;

import java.util.Set;
import java.util.TreeMap;

public class GLTreeMap<K,V> {
	private TreeMap<K,V> tm;
	
	public GLTreeMap(){
		this.tm = new TreeMap<K,V>();
	}
	
	public GLTreeMap<K,V> put(K k, V v){
		this.tm.put(k, v);
		return this;
	}
	
	public V get(K k){
		return this.tm.get(k);
	}
	
	public Set<K> keySet(){
		return this.tm.keySet();
	}
	
	public boolean containsKey(K k){
		return this.tm.containsKey(k);
	}
}
