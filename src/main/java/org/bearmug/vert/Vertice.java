package org.bearmug.vert;

public abstract class Vertice {
    private final String name;

    public Vertice(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public final int hashCode() {
        return name.hashCode();
    }

    @Override
    public final boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Vertice)) {
            return false;
        }
        Vertice other = (Vertice) obj;
        return name.equals(other.getName());
    }
}
