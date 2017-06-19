/**
 * Copyright 2011-2015 DEIB - Politecnico di Milano
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Acknowledgements:
 * We would like to thank Davide Barbieri, Emanuele Della Valle,
 * Marco Balduini, Soheila Dehghanzadeh, Shen Gao, and
 * Daniele Dell'Aglio for the effort in the development of the software.
 *
 * This work is partially supported by
 * - the European LarKC project (FP7-215535) of DEIB, Politecnico di
 * Milano
 * - the ERC grant “Search Computing” awarded to prof. Stefano Ceri
 * - the European ModaClouds project (FP7-ICT-2011-8-318484) of DEIB,
 * Politecnico di Milano
 * - the IBM Faculty Award 2013 grated to prof. Emanuele Della Valle;
 * - the City Data Fusion for Event Management 2013 project funded
 * by EIT Digital of DEIB, Politecnico di Milano
 * - the Dynamic and Distributed Information Systems Group of the
 * University of Zurich;
 * - INSIGHT NUIG and Science Foundation Ireland (SFI) under grant
 * No. SFI/12/RC/2289
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.larkc.csparql.core.old_parser;

/**
 * It represent the logical window of the stream with its range and window overlap identified
 * by the step field.
 * 
 * @author Marco
 */
public class LogicalWindow extends Window {

   private final RegistrationInfo.TimeUnit rangeTimeUnit;
   private final int step; // If it's equal to freq is't tumbling.
   private final RegistrationInfo.TimeUnit stepTimeUnit;

   public LogicalWindow(final int windowRange,
         final RegistrationInfo.TimeUnit rangeTimeUnit, final int step,
         final RegistrationInfo.TimeUnit stepTimeUnit) {
      this.rangeTimeUnit = rangeTimeUnit;
      this.step = step;
      this.stepTimeUnit = stepTimeUnit;
      this.windowRange = windowRange;
   }

   /**
    * @return the rangeTimeUnit
    */
   public RegistrationInfo.TimeUnit getRangeTimeUnit() {
      return this.rangeTimeUnit;
   }

   /**
    * @return the step
    */
   public int getStep() {
      return this.step;
   }

   /**
    * @return the stepTimeUnit
    */
   public RegistrationInfo.TimeUnit getStepTimeUnit() {
      return this.stepTimeUnit;
   }

   /**
    * Check if the windows are overlapping, with step != freq.
    * 
    * @return true if the windows doesn't overlap
    */
   public boolean isTumbling() {
      return this.windowRange == this.step && this.rangeTimeUnit.equals(this.stepTimeUnit);
   }

   @Override
   public String toString() {
      final StringBuffer sb = new StringBuffer();
      sb.append("Logical window, range = ");
      sb.append(this.windowRange);
      sb.append(" rangeTimeUnit = ");
      sb.append(this.rangeTimeUnit.toString());
      if (this.isTumbling()) {
         sb.append(" Tumbling");
      } else {
         sb.append(" step = ");
         sb.append(this.step);
         sb.append(" stepTimeUnit = ");
         sb.append(this.stepTimeUnit);
      }
      sb.append("\n");
      return sb.toString();
   }

}
