package net.shortninja.staffplus.server.listener;

import java.util.UUID;

import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.player.User;
import net.shortninja.staffplus.player.UserManager;
import net.shortninja.staffplus.player.attribute.gui.AbstractGui.AbstractAction;
import net.shortninja.staffplus.player.attribute.mode.ModeCoordinator;
import net.shortninja.staffplus.server.data.config.Options;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class InventoryClick implements Listener
{
	private Options options = StaffPlus.get().options;
	private UserManager userManager = StaffPlus.get().userManager;
	private ModeCoordinator modeCoordinator = StaffPlus.get().modeCoordinator;
	
	public InventoryClick()
	{
		Bukkit.getPluginManager().registerEvents(this, StaffPlus.get());
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onClick(InventoryClickEvent event)
	{
		Player player = (Player) event.getWhoClicked();
		UUID uuid = player.getUniqueId();
		User user = userManager.get(uuid);
		ItemStack item = event.getCurrentItem();
		int slot = event.getSlot();
		
		if(user.getCurrentGui() == null || item == null)
		{
			if(modeCoordinator.isInMode(uuid) && !options.modeInventoryInteraction)
			{
				event.setCancelled(true);
			}
			
			return;
		}
		
		AbstractAction action = user.getCurrentGui().getAction(slot);
		
		if(action != null)
		{
			action.click(player, item, slot);
			
			if(action.shouldClose())
			{
				player.closeInventory();
			}
		} 
		
		event.setCancelled(true);
	}
}