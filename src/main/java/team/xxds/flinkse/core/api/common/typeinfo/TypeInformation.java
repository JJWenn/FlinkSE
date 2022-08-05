package team.xxds.flinkse.core.api.common.typeinfo;

import java.io.Serializable;

public abstract class TypeInformation<T> {

    public abstract boolean isBasicType();

    public abstract boolean isTupleType();




    @Override
    public abstract String toString();

    @Override
    public abstract boolean equals(Object obj);

    @Override
    public abstract int hashCode();

}
