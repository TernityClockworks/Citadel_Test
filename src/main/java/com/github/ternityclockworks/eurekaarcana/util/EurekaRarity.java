package com.github.ternityclockworks.eurekaarcana.util;

import java.awt.Color;
import java.util.ArrayList;

import com.github.ternityclockworks.eurekaarcana.EurekaArcana;
import com.google.common.collect.Lists;

import net.minecraft.world.item.Rarity;

public enum EurekaRarity {
	
	CURIOUS("curious", 0x45FB33),
	FASCINATING("fascinating",6000F,120F/360F,85F/360F,1F,1F),
	ESOTERIC("esoteric", 0x581F8C),
	INCOMPREHENSIBLE("incomprehensible",8000F,1F,265F/360F,0.8F,0.7F,0.6F,0.4F);
	
	private static final String rarityIDPrefix = EurekaArcana.MODID + ":";
	private String rarityID = rarityIDPrefix + "uninstantiated";
	private int rarityColor = 0xFFFFFF;
	private ArrayList<Float> colorShiftParams = Lists.newArrayList(); // List of period, hue, etc. for color shifting
	
	/**
	 * 
	 * @param rarityID
	 * @param rarityColor
	 */
	private EurekaRarity(String rarityID, int rarityColor) {
		this.rarityID = rarityIDPrefix + rarityID;
		this.rarityColor = rarityColor;
	}
	
	/**
	 * 
	 * @param rarityID
	 * @param period
	 * @param maxHue
	 * @param minHue
	 * @param sat
	 * @param bri
	 */
	private EurekaRarity(String rarityID, float period, float maxHue, float minHue, float sat, float bri) {
		this.rarityID = rarityIDPrefix + rarityID;
		this.rarityColor = colorShift(period,maxHue,minHue,sat,bri);
		this.colorShiftParams.clear();
		this.colorShiftParams.add(period);
		this.colorShiftParams.add(maxHue);
		this.colorShiftParams.add(minHue);
		this.colorShiftParams.add(sat);
		this.colorShiftParams.add(bri);
	}
	
	private EurekaRarity(String rarityID, float period, float maxHue, float minHue, float maxSat, float minSat, float maxBri, float minBri) {
		this.rarityID = rarityIDPrefix + rarityID;
		this.rarityColor = colorShift(period,maxHue,minHue,maxSat,minSat,maxBri,minBri);
		this.colorShiftParams.clear();
		this.colorShiftParams.add(period);
		this.colorShiftParams.add(maxHue);
		this.colorShiftParams.add(minHue);
		this.colorShiftParams.add(maxSat);
		this.colorShiftParams.add(minSat);
		this.colorShiftParams.add(maxBri);
		this.colorShiftParams.add(minBri);
	}
	
	/**
	 * Use system time to generate a simple triangle wave that oscillates between max and min with the input period.
	 * Note that the exact period in ms may be off due to the variability of the currentTimeMillis method.
	 * @param period
	 * @param max Maximum value in degrees
	 * @param min Minimum value in degrees
	 * @return A triangle wave function describing the input behavior
	 */
	private static final float linearShift(float period, float max, float min) {
		return 2F*(max-min)/period*Math.abs((System.currentTimeMillis()%(int)period)-period/2F)+min;
	}
	
	/**
	 * Creates a function shifting with the parameters according to the linearShift method.
	 * @param period
	 * @param maxHue
	 * @param minHue
	 * @param sat
	 * @param bri
	 * @return An int representative of an RGB value that shifts over time.
	 */
	private static final int colorShift(float period, float maxHue, float minHue, float sat, float bri) {
		return Color.HSBtoRGB( linearShift(period,maxHue,minHue), sat, bri);
	}
	
	/**
	 * Creates a function shifting with the parameters according to the linearShift method.
	 * Includes specification for each component of hue saturation and brightness.
	 * @param period
	 * @param maxHue
	 * @param minHue
	 * @param maxSat
	 * @param minSat
	 * @param maxBri
	 * @param minBri
	 * @return An int representative of an RGB value that shifts over time.
	 */
	private static final int colorShift(float period, float maxHue, float minHue, float maxSat, float minSat, float maxBri, float minBri) {
		return Color.HSBtoRGB( linearShift(period,maxHue,minHue),
							   linearShift(period,maxSat,minSat),
							   linearShift(period,maxBri,minBri));
	}
	
	/**
	 * Creates a function shifting with the parameters according to the linearShift method.
	 * @return An int representative of an RGB value that shifts over time.
	 */
	public final int colorShift() {
		if (colorShiftParams.size() == 5) {
			return colorShift( colorShiftParams.get(0),
							   colorShiftParams.get(1),
							   colorShiftParams.get(2),
							   colorShiftParams.get(3),
							   colorShiftParams.get(4)
			);
		}
		else if (colorShiftParams.size() == 7) {
			return colorShift( colorShiftParams.get(0),
					   		   colorShiftParams.get(1),
					   		   colorShiftParams.get(2),
					   		   colorShiftParams.get(3),
					   		   colorShiftParams.get(4),
					   		   colorShiftParams.get(5),
					   		   colorShiftParams.get(6)
			);
		}
		else {
			return 0xFFFFFF;
		}
	}
	
	/**
	 * Creates a Rarity object from the input parameters.
	 * The rarity color becomes static when it is returned this way, so do not use with color shifting.
	 * @return
	 */
	public final Rarity getRarity() {
		return Rarity.create(this.rarityID, style -> style.withColor(this.rarityColor));
	}
	
	public final String getID() {
		return this.rarityID;
	}
	
	public final int getColor() {
		return this.rarityColor;
	}
	
}
