package de.jpx3.intave.block.fluid.next;

import de.jpx3.intave.block.variant.BlockVariant;
import de.jpx3.intave.block.variant.BlockVariantRegister;
import de.jpx3.intave.klass.rewrite.PatchyAutoTranslation;
import net.minecraft.server.v1_14_R1.Fluid;
import net.minecraft.server.v1_14_R1.IBlockData;
import net.minecraft.server.v1_14_R1.TagsFluid;
import org.bukkit.Material;

@PatchyAutoTranslation
final class v14LiquidResolver implements LiquidResolver {
  @Override
  @PatchyAutoTranslation
  public Liquid liquidFrom(Material type, int variantIndex) {
    IBlockData blockData = (IBlockData) BlockVariantRegister.rawVariantOf(type, variantIndex);
    if (blockData == null) {
      return Dry.of();
    }
    Fluid fluid = blockData.p();
    if (fluid == null) {
      return Dry.of();
    }
    BlockVariant variant = BlockVariantRegister.variantOf(type, variantIndex);
    boolean dry = fluid.isEmpty();
    boolean isWater = fluid.a(TagsFluid.WATER);
    boolean isLava = fluid.a(TagsFluid.LAVA);
    boolean source = fluid.isSource();
    boolean falling = variant.propertyOf("falling");
    float height = fluid.f();
    return select(isWater, isLava, dry, falling, height, source);
  }
}
