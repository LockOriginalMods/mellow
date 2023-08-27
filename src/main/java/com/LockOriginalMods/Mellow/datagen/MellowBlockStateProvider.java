package com.LockOriginalMods.Mellow.datagen;

import com.LockOriginalMods.Mellow.Init.BlockInit;
import com.LockOriginalMods.Mellow.Registry;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.generators.*;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.BiConsumer;

import static com.LockOriginalMods.Mellow.Mellow.MOD_ID;

public class MellowBlockStateProvider extends BlockStateProvider {

    public static final ResourceLocation BOTTOM = new ResourceLocation(MOD_ID, "block/machine_bottom");
    public static final ResourceLocation TOP = new ResourceLocation(MOD_ID, "block/machine_top");
    public static final ResourceLocation SIDE = new ResourceLocation(MOD_ID, "block/machine_side");


    public MellowBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, MOD_ID,exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        blockWithItem(BlockInit.RAW_TIN_BLOCK);
        blockWithItem(BlockInit.TIN_BLOCK);
        blockWithItem(BlockInit.DEEPSLATE_TIN_ORE);
        blockWithItem(BlockInit.TIN_ORE);
        blockWithItem(BlockInit.ALUMINUM_ORE);
        blockWithItem(BlockInit.DEEPSLATE_ALUMINUM_ORE);
        blockWithItem(BlockInit.ZINC_ORE);
        blockWithItem(BlockInit.DEEPSLATE_ZINC_ORE);
        registerGenerator();
        registerCharger();
    }

    @Override
    public void simpleBlockWithItem(Block block, ModelFile model) {
        super.simpleBlockWithItem(block, model);
    }
    private void blockWithItem(RegistryObject<Block> blockRegistryObject){
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }

    private void registerCharger() {
        BlockModelBuilder modelOn = models().slab(Registry.CHARGER_BLOCK.getId().getPath()+"_on", SIDE, BOTTOM, modLoc("block/charger_block_on")).texture("particle", SIDE);
        BlockModelBuilder modelOff = models().slab(Registry.CHARGER_BLOCK.getId().getPath()+"_off", SIDE, BOTTOM, modLoc("block/charger_block")).texture("particle", SIDE);
        getVariantBuilder(Registry.CHARGER_BLOCK.get()).forAllStates(state -> {
            ConfiguredModel.Builder<?> bld = ConfiguredModel.builder();
            bld.modelFile(state.getValue(BlockStateProperties.POWERED) ? modelOn : modelOff);
            return bld.build();
        });
    }

    private void registerGenerator() {
        BlockModelBuilder modelOn = models().cube(Registry.GENERATOR_BLOCK.getId().getPath()+"_on", BOTTOM, TOP, modLoc("block/generator_block_on"), SIDE, SIDE, SIDE).texture("particle", SIDE);
        BlockModelBuilder modelOff = models().cube(Registry.GENERATOR_BLOCK.getId().getPath()+"_off", BOTTOM, TOP, modLoc("block/generator_block"), SIDE, SIDE, SIDE).texture("particle", SIDE);
        directionBlock(Registry.GENERATOR_BLOCK.get(), (state, builder) -> {
            builder.modelFile(state.getValue(BlockStateProperties.POWERED) ? modelOn : modelOff);
        });
    }

    private VariantBlockStateBuilder directionBlock(Block block, BiConsumer<BlockState, ConfiguredModel.Builder<?>> model) {
        VariantBlockStateBuilder builder = getVariantBuilder(block);
        builder.forAllStates(state -> {
            ConfiguredModel.Builder<?> bld = ConfiguredModel.builder();
            model.accept(state, bld);
            applyRotationBld(bld, state.getValue(BlockStateProperties.FACING));
            return bld.build();
        });
        return builder;
    }

    private void applyRotationBld(ConfiguredModel.Builder<?> builder, Direction direction) {
        switch (direction) {
            case DOWN -> builder.rotationX(90);
            case UP -> builder.rotationX(-90);
            case NORTH -> { }
            case SOUTH -> builder.rotationY(180);
            case WEST -> builder.rotationY(270);
            case EAST -> builder.rotationY(90);
        }
    }
}

