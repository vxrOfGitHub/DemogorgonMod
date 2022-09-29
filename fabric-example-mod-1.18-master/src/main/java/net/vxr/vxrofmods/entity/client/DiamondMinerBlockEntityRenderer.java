package net.vxr.vxrofmods.entity.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import net.vxr.vxrofmods.block.custom.DiamondMinerBlock;
import net.vxr.vxrofmods.block.entity.DiamondMinerBlockEntity;

public class DiamondMinerBlockEntityRenderer implements BlockEntityRenderer <DiamondMinerBlockEntity> {

    public DiamondMinerBlockEntityRenderer(BlockEntityRendererFactory.Context context) {

    }

    @Override
    public void render(DiamondMinerBlockEntity entity, float tickDelta, MatrixStack matrices,
                       VertexConsumerProvider vertexConsumers, int light, int overlay) {
        ItemRenderer itemRenderer = MinecraftClient.getInstance().getItemRenderer();

        ItemStack itemStack = entity.getRenderStack();
        matrices.push();

        matrices.scale(0.4f, 0.4f, 0.4f);

        switch (entity.getCachedState().get(DiamondMinerBlock.FACING)) {
            case NORTH -> matrices.translate(2.3f, 1f, 1.5f);
            case EAST -> matrices.translate(1f, 1f, 2.3f);
            case SOUTH -> matrices.translate(0.19f, 1f, 1f);
            case WEST -> matrices.translate(1.5f, 0.9f, 0.19f);
        }

        switch (entity.getCachedState().get(DiamondMinerBlock.FACING)) {
            case NORTH -> matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(-27));
            case EAST -> matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(0));
            case SOUTH -> matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(27));
            case WEST -> matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(0));
        }

        switch (entity.getCachedState().get(DiamondMinerBlock.FACING)) {
            case NORTH -> matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(90));
            case EAST -> matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(0));
            case SOUTH -> matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(-90));
            case WEST -> matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(0));
        }

        switch (entity.getCachedState().get(DiamondMinerBlock.FACING)) {
            case NORTH -> matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(0));
            case EAST -> matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(-27));
            case SOUTH -> matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(0));
            case WEST -> matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(117));
        }
        itemRenderer.renderItem(itemStack, ModelTransformation.Mode.GUI, getLightLevel(entity.getWorld(), entity.getPos()),
                OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, 1);
        matrices.pop();
    }

    private int getLightLevel(World world, BlockPos pos) {
        int bLight = world.getLightLevel(LightType.BLOCK, pos);
        int sLight = world.getLightLevel(LightType.SKY, pos);
        return LightmapTextureManager.pack(bLight, sLight);
    }

}
