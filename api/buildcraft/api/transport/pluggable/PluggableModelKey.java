package buildcraft.api.transport.pluggable;

import java.util.Objects;

import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;

public abstract class PluggableModelKey {
    public final BlockRenderLayer layer;
    public final EnumFacing side;
    private final int hash;

    public PluggableModelKey(BlockRenderLayer layer, EnumFacing side) {
        if (layer != BlockRenderLayer.CUTOUT && layer != BlockRenderLayer.TRANSLUCENT) {
            throw new IllegalArgumentException("Can only use CUTOUT or TRANSLUCENT at the moment (was " + layer + ")");
        }
        if (side == null) throw new NullPointerException("side");
        this.layer = layer;
        this.side = side;
        this.hash = Objects.hash(layer, side);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        PluggableModelKey other = (PluggableModelKey) obj;
        return layer == other.layer && side == other.side;
    }

    @Override
    public int hashCode() {
        return hash;
    }
}
