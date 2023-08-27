package com.LockOriginalMods.Mellow.block.furnace;

import com.LockOriginalMods.Mellow.Init.ItemInit;
import com.LockOriginalMods.Mellow.Registry;
import com.LockOriginalMods.Mellow.data.ItemStackHandlerWrapper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ElectricFurnaceBlockEntity extends BlockEntity implements MenuProvider {

    private final ItemStackHandler itemHandler = new ItemStackHandler(2){
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 78;

    public ElectricFurnaceBlockEntity(BlockPos pos, BlockState state) {
        super(Registry.Electric_Furnace.get(),pos, state);
        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index){
                    case 0 -> ElectricFurnaceBlockEntity.this.progress;
                    case 1 -> ElectricFurnaceBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> ElectricFurnaceBlockEntity.this.progress = value;
                    case 1 -> ElectricFurnaceBlockEntity.this.maxProgress = value;
                }
            }
            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Electric furnace");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new ElectricFurnaceMenu(id, inventory, this, this.data);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == ForgeCapabilities.ITEM_HANDLER){
            return lazyItemHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(()-> itemHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        tag.put("inventory",itemHandler.serializeNBT());
        super.saveAdditional(tag);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        itemHandler.deserializeNBT(tag.getCompound("inventory"));
    }

    public void drops(){
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++){
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }
        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    public static  void tick(Level level, BlockPos pos, BlockState state, ElectricFurnaceBlockEntity pEntity) {
        if(level.isClientSide()){
            return;
        }
        if (hasRecipe(pEntity)){
            pEntity.progress++;
            setChanged(level, pos, state);
            if (pEntity.progress >= pEntity.maxProgress){
                smeltItem(pEntity);
            }else {
                pEntity.resetProgress();
                setChanged(level, pos, state);
            }
        }
    }

    private void resetProgress() {
        this.progress = 0;
    }

    private static void smeltItem(ElectricFurnaceBlockEntity pEntity) {
        if (hasRecipe(pEntity)){
            pEntity.itemHandler.extractItem(1, 1, false);
            pEntity.itemHandler.setStackInSlot(2, new ItemStack(ItemInit.ALUMINUM_INGOT.get(),
                    pEntity.itemHandler.getStackInSlot(2).getCount() + 1));
            pEntity.resetProgress();
        }
    }

    private static boolean hasRecipe(ElectricFurnaceBlockEntity entity) {
        SimpleContainer inventory  = new SimpleContainer(entity.itemHandler.getSlots());
        for (int i = 0; i < entity.itemHandler.getSlots(); i++) {
            inventory.setItem(i, entity.itemHandler.getStackInSlot(i));
        }
        boolean hasBatteryInFurnaceSlot = entity.itemHandler.getStackInSlot(1).getItem() == ItemInit.BATTERY.get();
        return hasBatteryInFurnaceSlot && canInsertAmountIntoOutputSlot(inventory)&&canInsertItemIntoOutputSlot(inventory, new ItemStack(ItemInit.BATTERY.get(),1));
    }

    private static boolean canInsertItemIntoOutputSlot(SimpleContainer inventory, ItemStack itemStack) {
        return inventory.getItem(2).getItem()== itemStack.getItem() || inventory.getItem(2).isEmpty();
    }

    private static boolean canInsertAmountIntoOutputSlot(SimpleContainer inventory) {
        return inventory.getItem(2).getMaxStackSize()>inventory.getItem(2).getCount();
    }
}
