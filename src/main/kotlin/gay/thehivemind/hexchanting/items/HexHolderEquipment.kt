package gay.thehivemind.hexchanting.items

import at.petrak.hexcasting.api.casting.ParticleSpray
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage
import at.petrak.hexcasting.api.casting.eval.vm.CastingVM
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.misc.MediaConstants
import gay.thehivemind.hexchanting.casting.PackagedToolCastEnv
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.Hand
import net.minecraft.util.math.Vec3d
import kotlin.math.absoluteValue
import kotlin.math.max
import kotlin.math.sign

// TODO: Repair will likely erase any hexes on them
// Enchanting works fine in testing.

// This code is mostly copied from Hex Casting's ItemPackagedHex

// Do these args really need to be nullable?
// Probably better to be safe, we can never trust Java code
// But also these functions are poorly defined for null arguments
// So let them just error

/*
The current intent is that these items can use their durability in the place of media, but it comes at a steep
conversion cost. This makes it strictly worse than artifacts and incentivises having a personal media supply.

If I make the tools use charged amethysts in their construction I don't need to feel too bad about making them on
par with diamond. It also fits their unstable nature, consuming themselves for power.
 */

interface HexHolderEquipment : HexImbuedItem {
    fun getDamageToMediaConversionFactor(): Long {
        return MediaConstants.DUST_UNIT / 10
    }

    // We want this to be a lower priority than anything else in your inventory
    override fun getConsumptionPriority(stack: ItemStack?): Int {
        return 500
    }

    fun damageToMedia(stack: ItemStack, damage: Int): Long {
        val remainingDurability = stack.maxDamage - damage
        return max(remainingDurability * getDamageToMediaConversionFactor(), 0)
    }

    fun mediaToDamage(stack: ItemStack, media: Long): Int {
        // Round up so people don't get to sneak free mana
        val damageEquivalent =
            media / getDamageToMediaConversionFactor() + media.rem(getDamageToMediaConversionFactor()).sign.absoluteValue
        return max(stack.maxDamage - damageEquivalent.toInt(), 0)
    }

    override fun getMedia(stack: ItemStack): Long {
        return damageToMedia(stack, stack.damage)
    }

    override fun getMaxMedia(stack: ItemStack): Long {
        return stack.maxDamage * getDamageToMediaConversionFactor()
    }

    override fun setMedia(stack: ItemStack, media: Long) {
        stack.damage = mediaToDamage(stack, media)
    }

    // We only want these items to be able to power themselves
    // It would be interesting if you could drain your gear with a careless hex,
    // but I don't  want to rewrite the inventory drain code to it right now
    override fun canProvideMedia(stack: ItemStack?): Boolean {
        return false
    }

    // You can recharge, but it'll be pricey
    // Better to repair with more materials than just shoving in media
    override fun canRecharge(stack: ItemStack?): Boolean {
        return true
    }

    override fun canDrawMediaFromInventory(stack: ItemStack?): Boolean {
        return true
    }

    fun scaffoldCasting(
        itemStack: ItemStack,
        world: ServerWorld,
        player: ServerPlayerEntity,
        stack: List<Iota>
    ) {
        val instructions = getHex(itemStack, world) ?: return
        // You can't mine or attack with the tool in your offhand so this should be safe
        // TODO: but not for armour. investigate
        val context = PackagedToolCastEnv(player, Hand.MAIN_HAND, itemStack)

        // Create empty casting image
        var castingImage = CastingImage()
        // prepare stack
        val castingStack = castingImage.stack.toMutableList()
        // We don't need to add the player to the stack, Mind's Reflection exists
        castingStack.addAll(stack)
        castingImage = castingImage.copy(stack = castingStack.toList())

        val vm = CastingVM(castingImage, context)
        val clientView = vm.queueExecuteAndWrapIotas(instructions, world)

        // We'll probably want to do something more subtle in future but this will work for now
        if (clientView.resolutionType.success) {
            ParticleSpray(player.pos, Vec3d(0.0, 1.5, 0.0), 0.4, Math.PI / 3, 30)
                .sprayParticles(world, context.getPigment())
        }
    }
}