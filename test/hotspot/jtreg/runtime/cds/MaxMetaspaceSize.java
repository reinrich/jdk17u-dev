/*
 * Copyright (c) 2015, 2023, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

/**
 * @test
 * @requires vm.cds
 * @requires vm.flagless
 * @bug 8067187 8200078
 * @summary Testing CDS dumping with the -XX:MaxMetaspaceSize=<size> option
 * @library /test/lib
 * @modules java.base/jdk.internal.misc
 *          java.management
 * @run driver MaxMetaspaceSize
 */

import java.util.ArrayList;

import jdk.test.lib.cds.CDSTestUtils;
import jdk.test.lib.process.ProcessTools;
import jdk.test.lib.Platform;

public class MaxMetaspaceSize {
  public static void main(String[] args) throws Exception {
    ArrayList<String> processArgs = new ArrayList<>();
    processArgs.add("-Xshare:dump");

    if (Platform.is64bit()) {
      processArgs.add("-XX:MaxMetaspaceSize=3m");
      processArgs.add("-XX:CompressedClassSpaceSize=1m");
    } else {
      processArgs.add("-XX:MaxMetaspaceSize=1m");
    }

    String msg = "OutOfMemoryError: Metaspace";
    ProcessBuilder pb = ProcessTools.createLimitedTestJavaProcessBuilder(processArgs);
    CDSTestUtils.executeAndLog(pb, "dump").shouldContain(msg).shouldHaveExitValue(1);
  }
}
