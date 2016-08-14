package bglutil.common.types;

import java.util.HashMap;
import java.util.Set;

public class GLHashMap<K,V>{

	private HashMap<K,V> hm;
	
	public GLHashMap(){
		this.hm = new HashMap<K,V>();
	}
	
	public GLHashMap<K,V> put(K k, V v){
		this.hm.put(k, v);
		return this;
	}
	
	public V get(K k){
		return this.hm.get(k);
	}
	
	public Set<K> keySet(){
		return this.hm.keySet();
	}
	
	public boolean containsKey(K k){
		return this.hm.containsKey(k);
	}
	
}
