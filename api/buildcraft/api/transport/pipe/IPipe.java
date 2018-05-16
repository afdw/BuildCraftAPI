package buildcraft.api.transport.pipe;

import javax.annotation.Nullable;

import net.minecraft.item.EnumDyeColor;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

import net.minecraftforge.common.capabilities.ICapabilityProvider;

public interface IPipe extends ICapabilityProvider {
    IPipeHolder getHolder();

    PipeDefinition getDefinition();

    PipeBehaviour getBehaviour();

    PipeFlow getFlow();

    @Nullable
    EnumDyeColor getColour();

    void setColour(@Nullable EnumDyeColor colour);

    void markForUpdate();

    @Nullable
    TileEntity getConnectedTile(EnumFacing side);

    @Nullable
    IPipe getConnectedPipe(EnumFacing side);

    boolean isConnected(EnumFacing side);

    ConnectedType getConnectedType(EnumFacing side);

    enum ConnectedType {
        TILE,
        PIPE
    }
}
