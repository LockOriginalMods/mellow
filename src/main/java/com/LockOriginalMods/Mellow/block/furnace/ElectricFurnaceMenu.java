package com.LockOriginalMods.Mellow.block.furnace;

import com.LockOriginalMods.Mellow.Init.BlockInit;
import com.LockOriginalMods.Mellow.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.SlotItemHandler;
import org.apache.commons.compress.utils.ByteUtils;
import org.jetbrains.annotations.Nullable;

public class ElectricFurnaceMenu extends AbstractContainerMenu {
    private final ElectricFurnaceBlockEntity blockEntity;
    private final Level level;
    private final ContainerData data;

    public ElectricFurnaceMenu(int id, Inventory inv, FriendlyByteBuf buffer) {
        this(id, inv, inv.player.level().getBlockEntity(buffer.readBlockPos()), new SimpleContainerData(7));
    }

    public ElectricFurnaceMenu(int id, Inventory inv, BlockEntity blockEntity, ContainerData data) {
        super(Registry.Electric_Furnace_Menu.get(), id);

        checkContainerDataCount(data, 1);
        this.blockEntity = (ElectricFurnaceBlockEntity)blockEntity;
        this.level = inv.player.level();
        this.data = data;

        addPlayerInventory(inv);
        addPlayerHotbar(inv);

        this.blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(itemHandler -> {
            this.addSlot(new SlotItemHandler(itemHandler, 0, 12, 15));
            this.addSlot(new SlotItemHandler(itemHandler, 1, 86, 15));
            this.addSlot(new SlotItemHandler(itemHandler, 2, 86, 60));
        });

        addDataSlots(this.data);
    }



    /**
     * @return Same as isCrafting but energy requirements are ignored
     */

    public boolean isSmelting(){
        return data.get(0)>0;
    }



    public int getScaledProgress() {
        int progress = this.data.get(0);
        int maxProgress = this.data.get(1);  // Max Progress
        int progressArrowSize = 26; // This is the height in pixels of your arrow

        return maxProgress != 0 && progress != 0 ? progress * progressArrowSize / maxProgress : 0;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot sourceSlot = slots.get(index);
        if(sourceSlot == null || !sourceSlot.hasItem())
            return ItemStack.EMPTY;

        ItemStack sourceItem = sourceSlot.getItem();
        ItemStack sourceItemCopy = sourceItem.copy();

        if(index < 4 * 9) {
            //Player inventory slot -> Merge into tile inventory
            if(!moveItemStackTo(sourceItem, 4 * 9, 4 * 9 + 1, false)) {
                //"+1" instead of "+2": Do not allow adding to output slot
                return ItemStack.EMPTY;
            }
        }else if(index < 4 * 9 + 2) {
            //Tile inventory slot -> Merge into player inventory
            if(!moveItemStackTo(sourceItem, 0, 4 * 9, false)) {
                return ItemStack.EMPTY;
            }
        }else {
            throw new IllegalArgumentException("Invalid slot index");
        }

        if(sourceItem.getCount() == 0)
            sourceSlot.set(ItemStack.EMPTY);
        else
            sourceSlot.setChanged();

        sourceSlot.onTake(player, sourceItem);

        return sourceItemCopy;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()), player, BlockInit.Electric_Furnace.get());
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for(int i = 0;i < 3;i++) {
            for(int j = 0;j < 9;j++) {
                addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for(int i = 0;i < 9;i++) {
            addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }

    public BlockEntity getBlockEntity() {
        return blockEntity;
    }
}





