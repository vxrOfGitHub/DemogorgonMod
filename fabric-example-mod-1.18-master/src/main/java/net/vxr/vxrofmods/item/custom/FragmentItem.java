package net.vxr.vxrofmods.item.custom;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class FragmentItem extends Item {

    public FragmentItem(Settings settings) {
        super(settings);
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return true;
    }
}
