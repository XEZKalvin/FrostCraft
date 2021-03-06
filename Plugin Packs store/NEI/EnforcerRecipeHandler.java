package xelitez.frostcraft.plugins;

import java.awt.Rectangle;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import xelitez.frostcraft.client.gui.GuiFrostEnforcer;
import xelitez.frostcraft.registry.RecipeRegistry;
import codechicken.core.ReflectionManager;
import codechicken.nei.NEIClientUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.forge.GuiContainerManager;
import codechicken.nei.recipe.TemplateRecipeHandler;

public class EnforcerRecipeHandler extends TemplateRecipeHandler 
{

	public class EnforcingPair extends CachedRecipe
	{
		public EnforcingPair(ItemStack ingred, ItemStack result)
		{
			ingred.stackSize = 1;
			this.ingred = new PositionedStack(ingred, 51, 6);
			this.result = new PositionedStack(result, 111, 24);
		}
		
		public PositionedStack getIngredient()
		{
			int cycle = cycleticks / 48;
			if(ingred.item.getItemDamage() == -1)
			{
				PositionedStack stack = ingred.copy();
				int maxDamage = 0;
				do
				{
					maxDamage++;
					stack.item.setItemDamage(maxDamage);
				}
				while(NEIClientUtils.isValidItem(stack.item));
				
				stack.item.setItemDamage(cycle % maxDamage);
				return stack;
			}
			return ingred;
		}
		
		public PositionedStack getResult()
		{
			return result;
		}
		
		PositionedStack ingred;
		PositionedStack result;
	}
	
	@Override
	public Class<? extends GuiContainer> getGuiClass()
	{
		return GuiFrostEnforcer.class;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public void loadCraftingRecipes(String outputId, Object... results)
	{
		if(outputId.equals("enforcing") && getClass() == EnforcerRecipeHandler.class)//don't want subclasses getting a hold of this
		{
			HashMap<List<Integer>, ItemStack> recipes = null;
			HashMap<Integer, ItemStack> recipesIdOnly = null;
			try
			{
				try
				{
					recipes = ReflectionManager.getField(RecipeRegistry.class, HashMap.class, RecipeRegistry.registry(), 3);
				}
				catch (ArrayIndexOutOfBoundsException e) {}
				try
				{
					recipesIdOnly = ReflectionManager.getField(RecipeRegistry.class, HashMap.class, RecipeRegistry.registry(), 4);
				}
				catch (ArrayIndexOutOfBoundsException e) {}
			}
			catch(Exception e)
			{
				e.printStackTrace();
				return;
			}
			if(recipesIdOnly != null)
			{
				for(Entry<Integer, ItemStack> recipe : recipesIdOnly.entrySet())
				{
					ItemStack item = recipe.getValue();
					arecipes.add(new EnforcingPair(new ItemStack(recipe.getKey(), 1, -1), item));
				}
			}
			if(recipes == null)return;
			for(Entry<List<Integer>, ItemStack> recipe : recipes.entrySet())
			{
				ItemStack item = recipe.getValue();
				arecipes.add(new EnforcingPair(new ItemStack(recipe.getKey().get(0), 1, recipe.getKey().get(1)), item));
			}
		}
		else
		{
			super.loadCraftingRecipes(outputId, results);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void loadCraftingRecipes(ItemStack result)
	{
		HashMap<List<Integer>, ItemStack> recipes = null;
		HashMap<Integer, ItemStack> recipesIdOnly = null;
		try
		{
			try
			{
				recipes = ReflectionManager.getField(RecipeRegistry.class, HashMap.class, RecipeRegistry.registry(), 3);
			}
			catch (ArrayIndexOutOfBoundsException e) {}
			try
			{
				recipesIdOnly = ReflectionManager.getField(RecipeRegistry.class, HashMap.class, RecipeRegistry.registry(), 4);
			}
			catch (ArrayIndexOutOfBoundsException e) {}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return;
		}
		if(recipesIdOnly != null)
		{
			for(Entry<Integer, ItemStack> recipe : recipesIdOnly.entrySet())
			{
				ItemStack item = recipe.getValue();
				if(item.itemID == result.itemID)
				{
					arecipes.add(new EnforcingPair(new ItemStack(recipe.getKey(), 1, -1), item));
				}
			}
		}
		if(recipes == null)return;
		for(Entry<List<Integer>, ItemStack> recipe : recipes.entrySet())
		{
			ItemStack item = recipe.getValue();
			if(NEIClientUtils.areStacksSameType(item, result))
			{
				arecipes.add(new EnforcingPair(new ItemStack(recipe.getKey().get(0), 1, recipe.getKey().get(1)), item));
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void loadUsageRecipes(ItemStack ingredient)
	{
		HashMap<List<Integer>, ItemStack> recipes = null;
		HashMap<Integer, ItemStack> recipesIdOnly = null;
		try
		{
			try
			{
				recipes = (HashMap<List<Integer>, ItemStack>) ReflectionManager.getField(RecipeRegistry.class, HashMap.class, RecipeRegistry.registry(), 3);
			}
			catch (ArrayIndexOutOfBoundsException e) {
			}
			try
			{
				recipesIdOnly = ReflectionManager.getField(RecipeRegistry.class, HashMap.class, RecipeRegistry.registry(), 4);
			}
			catch (ArrayIndexOutOfBoundsException e) {}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return;
		}
		if(recipesIdOnly != null)
		{
			for(Entry<Integer, ItemStack> recipe : recipesIdOnly.entrySet())
			{
				ItemStack item = recipe.getValue();
				if(ingredient.itemID == recipe.getKey())
				{
					arecipes.add(new EnforcingPair(ingredient, item));
				}
			}
		}
		if(recipes == null)return;
		for(Entry<List<Integer>, ItemStack> recipe : recipes.entrySet())
		{
			ItemStack item = recipe.getValue();
			if(ingredient.itemID == recipe.getKey().get(0) && ingredient.getItemDamage() == recipe.getKey().get(1))
			{
				arecipes.add(new EnforcingPair(ingredient, item));
			}
		}
	}
	
	@Override
	public void loadTransferRects()
	{
		transferRects.add(new RecipeTransferRect(new Rectangle(74, 23, 24, 18), "enforcing"));
	}

	@Override
	public String getRecipeName() 
	{
		return "Enforcing";
	}
	
	@Override
	public String getOverlayIdentifier()
	{
		return "enforcing";
	}

	@Override
	public String getGuiTexture() 
	{
		return "frostcraft:textures/frostenforcer.png";
	}
	
	public void drawExtras(GuiContainerManager gui, int recipe)
	{
		drawProgressBar(52, 26, 176, 0, 14, 14, 48, 7);
		drawProgressBar(74, 23, 176, 14, 24, 16, 48, 0);
	}

}
