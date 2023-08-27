package com.LockOriginalMods.Mellow.datagen;

import com.LockOriginalMods.Mellow.Init.BlockInit;
import com.LockOriginalMods.Mellow.Init.ItemInit;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraftforge.registries.RegistryObject;

import java.util.Map;
import java.util.Set;

public class MellowBlockLootTables extends BlockLootSubProvider {

    protected MellowBlockLootTables() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    protected MellowBlockLootTables(Set<Item> p_281507_, FeatureFlagSet p_283552_, Map<ResourceLocation, LootTable.Builder> p_282212_) {
        super(p_281507_, p_283552_, p_282212_);
    }

    @Override
    protected void generate() {
        dropSelf(BlockInit.TIN_BLOCK.get());
        dropSelf(BlockInit.RAW_TIN_BLOCK.get());


        add(BlockInit.DEEPSLATE_TIN_ORE.get(),(block -> createOreDrop(BlockInit.DEEPSLATE_TIN_ORE.get(), ItemInit.RAW_TIN.get())));
        add(BlockInit.TIN_ORE.get(),(block -> createOreDrop(BlockInit.TIN_ORE.get(), ItemInit.RAW_TIN.get())));
        add(BlockInit.DEEPSLATE_ZINC_ORE.get(),(block -> createOreDrop(BlockInit.DEEPSLATE_ZINC_ORE.get(), ItemInit.RAW_ZINC.get())));
        add(BlockInit.ZINC_ORE.get(),(block -> createOreDrop(BlockInit.ZINC_ORE.get(), ItemInit.RAW_ZINC.get())));
        add(BlockInit.DEEPSLATE_ALUMINUM_ORE.get(),(block -> createOreDrop(BlockInit.DEEPSLATE_ALUMINUM_ORE.get(), ItemInit.RAW_ALUMINUM.get())));
        add(BlockInit.ALUMINUM_ORE.get(),(block -> createOreDrop(BlockInit.ALUMINUM_ORE.get(), ItemInit.RAW_ALUMINUM.get())));

    }



    @Override
    protected Iterable<Block> getKnownBlocks() {
         return BlockInit.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }

    protected void dropOther(Block p_248885_, ItemLike p_251883_,ItemLike p_251883__) {
        this.add(p_248885_, this.createSingleItemTable(p_251883_, p_251883__));
    }

    private LootTable.Builder createSingleItemTable(ItemLike p_251883_, ItemLike p_251883__) {
        return null;
    }
}
