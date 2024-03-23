package club.someoneice.vine.common.item.cocktail;

import club.someoneice.vine.init.BlockInit;
import club.someoneice.vine.init.ItemInit;
import club.someoneice.vine.util.Utilities;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.ArrayList;
import java.util.List;

public class Cocktail {
    public static class CocktailBlock extends Block {
        int hunger;

        public CocktailBlock(int hunger) {
            super(Properties.copy(Blocks.GLASS).noOcclusion());
            this.hunger = hunger;
        }

        @Override
        public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
            if (!world.isClientSide) {
                player.getFoodData().eat(hunger, hunger - 2);
                world.setBlock(pos, BlockInit.GobletBlock.get().defaultBlockState(), 0);
                world.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
                world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.GENERIC_DRINK, SoundSource.NEUTRAL, 1.0F, 1.0F);
            }

            return InteractionResult.SUCCESS;
        }

        @Override
        public List<ItemStack> getDrops(BlockState state, LootParams.Builder loot) {
            var itemList = new ArrayList<ItemStack>();
            itemList.add(new ItemStack(this.asItem()));
            return itemList;
        }

        @Override
        public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
            return Block.box(6, 0, 6, 10, 6, 10);
        }

        @Override
        public RenderShape getRenderShape(BlockState state) {
            return RenderShape.MODEL;
        }
    }

    public static class CocktailItem extends BlockItem {
        public CocktailItem(CocktailBlock block) {
            super(block, propertiesHelper(block.hunger));
        }

        public UseAnim getUseAnimation(ItemStack item) {
            return UseAnim.DRINK;
        }

        public ItemStack finishUsingItem(ItemStack item, Level world, LivingEntity entity) {
            super.finishUsingItem(item, world, entity);
            if (entity instanceof Player player)
                Utilities.addItem2PlayerOrDrop(player, new ItemStack(ItemInit.Goblet.get()));
            return item;
        }

        private static Item.Properties propertiesHelper(int hunger) {
            var properties = new Item.Properties();
            var builder = new FoodProperties.Builder();
            builder.nutrition(hunger);
            builder.saturationMod(Math.max(hunger - 2, 0));
            builder.alwaysEat();
            properties.food(builder.build());
            properties.stacksTo(8);
            return properties;
        }
    }
}
