package club.someoneice.vine.common.shaker;

import club.someoneice.vine.core.TskimiSeiranVine;
import club.someoneice.vine.init.BlockInit;
import club.someoneice.vine.init.ItemInit;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;

public class Cocktail {
    public static class CocktailBlock extends Block {
        String name;
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
                world.gameEvent(GameEvent.BLOCK_CHANGE, pos);
                world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.GENERIC_DRINK, SoundSource.NEUTRAL, 1.0F, 1.0F);
            }

            return InteractionResult.SUCCESS;
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
            if (entity instanceof Player player) player.addItem(new ItemStack(ItemInit.Goblet.get()));
            return item;
        }

        private static Item.Properties propertiesHelper(int hunger) {
            var properties = new Item.Properties();
            var builder = new FoodProperties.Builder();
            builder.nutrition(hunger);
            builder.saturationMod(Math.max(hunger - 2, 0));
            builder.alwaysEat();
            properties.food(builder.build());
            properties.tab(TskimiSeiranVine.WINE_TAB);
            properties.stacksTo(8);
            return properties;
        }
    }
}
