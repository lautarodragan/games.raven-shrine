/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rs;

/**
 *
 * @author lainmaster
 */
public interface RavenShrineConstants {
	public static final int RS_TILE_SIZE = 32;
	public static final int RS_AUTOTILE_BIT = 0x8000;
	public static final int RS_AUTOTILE_MANUAL_BIT = 0x4000;
	public static final int RS_NULL_TILE = 0xFFFF;

	public static final int RS_TILE_PASSABLE_BIT = 0x01;
	public static final int RS_TILE_PASSABLE_N_BIT = 0x02;
	public static final int RS_TILE_PASSABLE_S_BIT = 0x04;
	public static final int RS_TILE_PASSABLE_W_BIT = 0x08;
	public static final int RS_TILE_PASSABLE_E_BIT = 0x10;

	public static final int RS_DIR_W = 3;
	public static final int RS_DIR_N = 2;
	public static final int RS_DIR_E = 1;
	public static final int RS_DIR_S = 0;

}
