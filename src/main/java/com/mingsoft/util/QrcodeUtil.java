/**
The MIT License (MIT) * Copyright (c) 2016 铭飞科技(mingsoft.net)

 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:

 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.

 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.mingsoft.util;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

/**
 * 利用zxing开源工具生成二维码QRCode
 * @author 王天培QQ:78750478
 * @version 
 * 版本号：100-000-000<br/>
 * 创建日期：2012-03-15<br/>
 * 历史修订：<br/>
 */
public class QrcodeUtil {
	private static final int BLACK = 0xff000000;
	private static final int WHITE = 0xFFFFFFFF;

	/**
	 * 生成QRCode二维码<br>
	 * 在编码时需要将com.google.zxing.qrcode.encoder.Encoder.java中的<br>
	 * static final String DEFAULT_BYTE_MODE_ENCODING = "ISO8859-1";<br>
	 * 修改为UTF-8，否则中文编译后解析不了<br>
	 * @param contents 内容
	 * @param file 文件
	 * @param format 格式
	 * @param width 宽度
	 * @param height 高度
	 * @param hints 提示信息 
	 */
	public static void encode(String contents, File file, BarcodeFormat format, int width, int height, Map<EncodeHintType, ?> hints) {
		try {
			BitMatrix bitMatrix = new MultiFormatWriter().encode(contents, format, width, height);
			BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					image.setRGB(x, y, bitMatrix.get(x, y) == true ? BLACK : WHITE);
				}
			}
			ImageIO.write(image, "png", file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 解析QRCode二维码
	 * @param file 二维码
	 */
	@SuppressWarnings("unchecked")
	public void decode(File file) {
		try {
			BufferedImage image;
			try {
				image = ImageIO.read(file);
				if (image == null) {
					System.out.println("Could not decode image");
				}
				LuminanceSource source = new BufferedImageLuminanceSource(image);
				BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
				Result result;
				@SuppressWarnings("rawtypes")
				Hashtable hints = new Hashtable();
				// 解码设置编码方式为：utf-8
				hints.put(DecodeHintType.CHARACTER_SET, "utf-8");
				result = new MultiFormatReader().decode(bitmap, hints);
				String resultStr = result.getText();
				System.out.println("解析后内容：" + resultStr);
			} catch (IOException ioe) {
				System.out.println(ioe.toString());
			} catch (ReaderException re) {
				System.out.println(re.toString());
			}
		} catch (Exception ex) {
			System.out.println(ex.toString());
		}
	}
}