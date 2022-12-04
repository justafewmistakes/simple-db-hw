package simpledb;

import java.io.Serializable;
import java.util.*;

/**
 * TupleDesc describes the schema of a tuple.
 * TupleDesc描述元组的模式
 */
public class TupleDesc implements Serializable {

    /**
     * A help class to facilitate organizing the information of each field
     * */
    public static class TDItem implements Serializable {

        private static final long serialVersionUID = 1L;

        /**
         * The type of the field
         * */
        public final Type fieldType;
        
        /**
         * The name of the field
         * */
        public final String fieldName;

        public TDItem(Type t, String n) {
            this.fieldName = n;
            this.fieldType = t;
        }

        public String toString() {
            return fieldName + "(" + fieldType + ")";
        }
    }

    /**
     * @return
     *        An iterator which iterates over all the field TDItems
     *        that are included in this TupleDesc
     * */
    public Iterator<TDItem> iterator() {
        // some code goes here
        return Arrays.asList(tdItems).iterator();
    }

    private static final long serialVersionUID = 1L;
    private final TDItem[] tdItems;

    /**
     * Create a new TupleDesc with typeAr.length fields with fields of the
     * specified types, with associated named fields.
     * 
     * @param typeAr
     *            array specifying the number of and types of fields in this
     *            TupleDesc. It must contain at least one entry.
     * @param fieldAr
     *            array specifying the names of the fields. Note that names may
     *            be null.
     */
    public TupleDesc(Type[] typeAr, String[] fieldAr) {
        // some code goes here
//        if(typeAr.length == 0) {
//            throw new RuntimeException("in order to create TupleDesc, typeAr need at least one item");
//        }
        tdItems = new TDItem[typeAr.length];
        for(int i=0;i<tdItems.length;++i) {
            tdItems[i] = new TDItem(typeAr[i], fieldAr[i]);
        }
    }

    /**
     * Constructor. Create a new tuple desc with typeAr.length fields with
     * fields of the specified types, with anonymous (unnamed) fields.
     * 
     * @param typeAr
     *            array specifying the number of and types of fields in this
     *            TupleDesc. It must contain at least one entry.
     */
    public TupleDesc(Type[] typeAr) {
        // some code goes here
        tdItems = new TDItem[typeAr.length];
        for(int i=0;i<tdItems.length;++i) {
            tdItems[i] = new TDItem(typeAr[i], "");
        }
    }

    /**
     * @return the number of fields in this TupleDesc
     */
    public int numFields() {
        // some code goes here
        return tdItems.length;
    }

    /**
     * Gets the (possibly null) field name of the ith field of this TupleDesc.
     * 
     * @param i
     *            index of the field name to return. It must be a valid index.
     * @return the name of the ith field
     * @throws NoSuchElementException
     *             if i is not a valid field reference.
     */
    public String getFieldName(int i) throws NoSuchElementException {
        // some code goes here
        if(i >= numFields() || i < 0) {
            throw new NoSuchElementException("biggest index is " + (numFields()-1) + ", please check your pos " + i);
        }
        return tdItems[i].fieldName;
    }

    /**
     * Gets the type of the ith field of this TupleDesc.
     * 
     * @param i
     *            The index of the field to get the type of. It must be a valid
     *            index.
     * @return the type of the ith field
     * @throws NoSuchElementException
     *             if i is not a valid field reference.
     */
    public Type getFieldType(int i) throws NoSuchElementException {
        // some code goes here
        if(i >= numFields() || i < 0) {
            throw new NoSuchElementException("biggest index is " + (numFields()-1) + ", please check your pos " + i);
        }
        return tdItems[i].fieldType;
    }

    /**
     * Find the index of the field with a given name.
     * 
     * @param name
     *            name of the field.
     * @return the index of the field that is first to have the given name.
     * @throws NoSuchElementException
     *             if no field with a matching name is found.
     */
    public int fieldNameToIndex(String name) throws NoSuchElementException {
        // some code goes here
        for(int i=0;i<tdItems.length;++i) {
            if(name != null && name.equals(tdItems[i].fieldName)) {
                return i;
            }
            // 此处有点奇怪，在TupleDescTest测试中的nameToId（）中，表示null不是一个正确的tupleDesc的field name，
            // 然而，在tupleDesc中却明确表示field name可能为null。
//            else if (name == null && tdItems[i].fieldName == null) {
//                return i;
//            }
        }
        throw new NoSuchElementException("not find a filed named " + name);
    }

    /**
     * @return The size (in bytes) of tuples corresponding to this TupleDesc.
     *         Note that tuples from a given TupleDesc are of a fixed size.
     */
    public int getSize() {
        // some code goes here
        // note that string type in this lab has 128+4 bytes
        int size = 0;
        Iterator<TDItem> iterator = iterator();
        while(iterator.hasNext()) {
            TDItem tdItem = iterator.next();
            size += tdItem.fieldType.getLen();
        }
        return size;
    }

    /**
     * Merge two TupleDescs into one, with td1.numFields + td2.numFields fields,
     * with the first td1.numFields coming from td1 and the remaining from td2.
     * 
     * @param td1
     *            The TupleDesc with the first fields of the new TupleDesc
     * @param td2
     *            The TupleDesc with the last fields of the TupleDesc
     * @return the new TupleDesc
     */
    public static TupleDesc merge(TupleDesc td1, TupleDesc td2) {
        // some code goes here
        Type[] typeAr = new Type[td1.numFields() + td2.numFields()];
        String[] filedAr = new String[td1.numFields() + td2.numFields()];
        for(int i=0;i<td1.numFields();++i) {
            typeAr[i] = td1.getFieldType(i);
            filedAr[i] = td1.getFieldName(i);
        }
        for(int i=0;i<td2.numFields();++i) {
            typeAr[i+td1.numFields()] = td2.getFieldType(i);
            filedAr[i+td1.numFields()] = td2.getFieldName(i);
        }
        return new TupleDesc(typeAr, filedAr);
    }

    /**
     * Compares the specified object with this TupleDesc for equality. Two
     * TupleDescs are considered equal if they have the same number of items
     * and if the i-th type in this TupleDesc is equal to the i-th type in o
     * for every i.
     * 
     * @param o
     *            the Object to be compared for equality with this TupleDesc.
     * @return true if the object is equal to this TupleDesc.
     */

    public boolean equals(Object o) {
        // some code goes here
        if(o instanceof TupleDesc) {
            TupleDesc td = (TupleDesc) o;
            if(this.numFields() == td.numFields()) {
                for(int i=0;i<this.numFields();++i) {
                    if(!this.tdItems[i].fieldType.equals(td.tdItems[i].fieldType)) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    public int hashCode() {
        // If you want to use TupleDesc as keys for HashMap, implement this so
        // that equal objects have equals hashCode() results
        throw new UnsupportedOperationException("unimplemented");
    }

    /**
     * Returns a String describing this descriptor. It should be of the form
     * "fieldType[0](fieldName[0]), ..., fieldType[M](fieldName[M])", although
     * the exact format does not matter.
     * 
     * @return String describing this descriptor.
     */
    public String toString() {
        // some code goes here
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<numFields()-1;++i) {
            sb.append("name: ").append(tdItems[i].fieldName).append("type: ")
                    .append(tdItems[i].fieldType).append(" ");
        }
        sb.append("name: ").append(tdItems[numFields() - 1].fieldName).append("type: ")
                .append(tdItems[numFields() - 1].fieldType).append(".");
        return sb.toString();
    }
}
