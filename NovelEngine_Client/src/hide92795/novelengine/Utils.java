//
// NovelEngine Project
//
// Copyright (C) 2013 - hide92795
//
//    Licensed under the Apache License, Version 2.0 (the "License");
//    you may not use this file except in compliance with the License.
//    You may obtain a copy of the License at
//
//        http://www.apache.org/licenses/LICENSE-2.0
//
//    Unless required by applicable law or agreed to in writing, software
//    distributed under the License is distributed on an "AS IS" BASIS,
//    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//    See the License for the specific language governing permissions and
//    limitations under the License.
//
package hide92795.novelengine;

import hide92795.novelengine.loader.Loader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.security.Key;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;

/**
 * 　多クラスから利用される機能を提供します。
 * 
 * @author hide92795
 */
public final class Utils {
	/**
	 * 乱数生成用の {@link java.util.Random Random} インスタンスです。
	 */
	private static Random random = new Random();

	/**
	 * このクラスはユーティリティクラスのためオブジェクト化できません。
	 */
	private Utils() {
	}

	/**
	 * 0から指定された値までの乱数を生成します。
	 * 
	 * @param num
	 *            乱数の上限数
	 * @return 生成された乱数
	 */
	public static int getRandom(int num) {
		return random.nextInt(num);
	}

	/**
	 * 発生した例外をロガーに出力します。
	 * 
	 * @param e
	 *            発生した例外
	 */
	public static void printStackTraceToLogger(Exception e) {
		Logger.error(e);
		StackTraceElement[] trace = e.getStackTrace();
		for (int i = 0; i < trace.length; i++) {
			Logger.error("\tat " + trace[i]);
		}

		Throwable ourCause = e.getCause();
		if (ourCause != null) {
			printStackTraceToLoggerAsCause(ourCause, trace);
		}
	}

	/**
	 * 元の例外をスローさせた原因となる{@link java.lang.Throwable Throwable}を出力します。
	 * 
	 * @see <a href="http://stackoverflow.com/questions/6691150"> http://stackoverflow.com/questions/6691150 </a>
	 * 
	 * @param t
	 *            例外の原因
	 * @param causedTrace
	 *            元の例外の要素
	 */
	private static void printStackTraceToLoggerAsCause(Throwable t, StackTraceElement[] causedTrace) {
		StackTraceElement[] trace2 = t.getStackTrace();
		int m = trace2.length - 1, n = causedTrace.length - 1;
		while (m >= 0 && n >= 0 && trace2[m].equals(causedTrace[n])) {
			m--;
			n--;
		}
		int framesInCommon = trace2.length - 1 - m;

		Logger.error("Caused by: " + t);
		for (int i = 0; i <= m; i++) {
			Logger.error("\tat " + trace2[i]);
		}
		if (framesInCommon != 0) {
			Logger.error("\t... " + framesInCommon + " more");
		}

		Throwable ourCause = t.getCause();
		if (ourCause != null) {
			printStackTraceToLoggerAsCause(ourCause, causedTrace);
		}
	}

	/**
	 * 暗号化しながら指定したファイルに書き込みを行うストリームを作成します。
	 * 
	 * @param file
	 *            書き込み先のファイル
	 * @return 暗号化を行うストリーム
	 * @throws Exception
	 *             何らかのエラーが発生した場合
	 */
	public static CipherOutputStream createCipherOutputStream(File file) throws Exception {
		return createCipherOutputStream(new FileOutputStream(file));
	}

	/**
	 * 暗号化しながら指定したストリームに書き込みを行うストリームを作成します。
	 * 
	 * @param os
	 *            書き込み先のストリーム
	 * @return 暗号化を行うストリーム
	 * @throws Exception
	 *             何らかのエラーが発生した場合
	 */
	public static CipherOutputStream createCipherOutputStream(OutputStream os) throws Exception {
		Key key = Loader.getKey();
		Cipher cipher = Cipher.getInstance("AES/PCBC/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, key);
		CipherOutputStream cos = new CipherOutputStream(os, cipher);
		os.write(cipher.getIV());
		return cos;
	}
}
