package club.someoneice.vine.init;

import club.someoneice.vine.common.block.barrel.BrewingBarrelEntity;
import club.someoneice.vine.common.block.boilers.DistillationBoilerEntity;
import club.someoneice.vine.core.TskimiSeiranVine;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class TileInit {
    public static final DeferredRegister<BlockEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, TskimiSeiranVine.MODID);
    public static final RegistryObject<BlockEntityType<BrewingBarrelEntity>> BrewingBarrel = TILE_ENTITIES.register("tile_brewing_barrel", () -> BlockEntityType.Builder.of(BrewingBarrelEntity::new, BlockInit.BrewingBarrelBlock.get()).build(null));
    public static final RegistryObject<BlockEntityType<DistillationBoilerEntity>> DistillationBoiler = TILE_ENTITIES.register("tile_distillation_boriler", () -> BlockEntityType.Builder.of(DistillationBoilerEntity::new, BlockInit.DistillationBoilerBlock.get()).build(null));
}
