package com.csa.util;

import java.util.ArrayList;
import java.util.Map;
import com.csa.entity.Bowl;
import com.csa.entity.Innings;
import com.csa.entity.MatchDetails;
import com.csa.visualization.BattingSegment;
import com.csa.visualization.InningByInningsResults;

public class InningsUtil {

	public static InningByInningsResults generateInningsByInningsResults(int matchID, int side,
			Innings innings) {

		InningByInningsResults inningsFactorSet = new InningByInningsResults();
		int iningID = innings.getInning();

		inningsFactorSet.setBalls(innings.getBalls());
		inningsFactorSet.setComplete(innings.getComplete());
		inningsFactorSet.setSegment(innings.getSegment());

		// inningsId will be autoGenerated

		inningsFactorSet.setMatchId(matchID);
		inningsFactorSet.setFirstInningsOrSecondInnings(iningID);

		System.out.println("id: " + matchID);
		ArrayList<BattingSegment> segmentList = getBattingSegmentList(innings.getDeliveries());

		// set number of batting segments
		inningsFactorSet.setNumberOfBattingSegments(segmentList.size());

		// set avg mean runs in batting segment
		inningsFactorSet
				.setAvgMeanRunsInBattingSegment(getMeanRunsInSegment(segmentList));

		// set avg runs in batting segment
		inningsFactorSet
				.setAvgRunsInBattingSegment(getAvgRunsInSegment(segmentList));

		// set avg pressure factor
		inningsFactorSet
				.setAvgPressureFactor(getAvgPressureFactor(segmentList));

		// set dot bowl presentage
		inningsFactorSet.setDotBowlPrerentage(getDotBowlPresentage(innings.getDeliveries()));

		// number of Wickets lost in first Innings
		inningsFactorSet.setNumberOfWicketsLost(innings.getNumberOfWickets());

		// sixHittingFrequency
		Double sixHittingFrequency = getInningsSixHittingFrequency(innings.getDeliveries());
		inningsFactorSet.setSixHittingFrequency(sixHittingFrequency);

		// fourHittingFrequency
		Double fourHittingFrequency = getInningsFourHittingFrequency(innings.getDeliveries());
		inningsFactorSet.setFourHittingFrequency(fourHittingFrequency);

		// Boundary Percentage
		Double boundrayPresentage = getBoundrayRunsPresentage(innings.getDeliveries(), innings
				.getNumberOfRunsScored());
		inningsFactorSet.setBoundaryRunsPresentage(boundrayPresentage);

		// DotBawlToRunsRatio
		int numberOfDots = getNumberOfdots(innings.getDeliveries());
		int inningsScore = innings.getNumberOfRunsScored();

		Double dotBowlToRunsRatio = getDotBowlToRunsRatio(numberOfDots,
				inningsScore);

		inningsFactorSet.setDotBowlToRunsRatio(dotBowlToRunsRatio);

		if(iningID==1){
			if (side == 1) {
				inningsFactorSet.setWinOrLoss("win");
			} else if (side == 2) {
				inningsFactorSet.setWinOrLoss("loss");
			} else {
				inningsFactorSet.setWinOrLoss("draw");
			}
		}
		else{
			// team 1 wins
			if (side == 2) {
				inningsFactorSet.setWinOrLoss("win");
			} else if (side == 1) {
				inningsFactorSet.setWinOrLoss("loss");
			} else {
				inningsFactorSet.setWinOrLoss("draw");
			}
		}

		System.out.println(inningsFactorSet.getAvgMeanRunsInBattingSegment() + " " + inningsFactorSet.getAvgPressureFactor() + " " + inningsFactorSet.getDotBowlToRunsRatio()+ " " + inningsFactorSet.getNumberOfBattingSegments());
		return inningsFactorSet;
	}

	public static ArrayList<BattingSegment> getBattingSegmentList(
			Map<Integer, com.csa.entity.Bowl> deliveries) {

		ArrayList<BattingSegment> compressSet = new ArrayList<>();
		BattingSegment lastElement;

		System.out.println("sizeeee: " + deliveries.size());
		for (int i = 1; i <= deliveries.size(); i++) {


			if (deliveries.get(i)!=null) {

				if (compressSet.size() != 0) {
					lastElement = compressSet.get(compressSet.size() - 1);
				} else {
					lastElement = new BattingSegment();
					lastElement.setBatsmansName("NO_BATSMAN");
				}

				Bowl bowl = deliveries.get(i);
				String batsman = bowl.getBatsman();

				if (lastElement.getBatsmansName().equals(batsman)) {
					// doNothing
					int currentRunsInTheSegment = lastElement
							.getNumberOfRunsScoredInSegment();
					int currentNumberOfExtrasInSegment = lastElement
							.getNumberOfExtras();
					int currentNumberOfDots = lastElement.getNumberOfDotBowls();
					int currentNumberOfBowls = lastElement.getNumberOfBowls();

					lastElement
							.setNumberOfRunsScoredInSegment(currentRunsInTheSegment
									+ bowl.getRuns());
					lastElement.setNumberOfExtras(currentNumberOfExtrasInSegment
							+ bowl.getExtras());

					if (bowl.getTotalRuns() == 0) {
						lastElement.setNumberOfDotBowls(currentNumberOfDots + 1);
					}
					// this need to be change after find out about type of extras
					lastElement.setNumberOfDotBowls(currentNumberOfBowls + 1);
				} else {
					// create new batting segment
					BattingSegment segment = new BattingSegment();
					segment.setBatsmansName(batsman);
					segment.setNumberOfRunsScoredInSegment(bowl.getRuns());
					segment.setNumberOfExtras(bowl.getExtras());

					if (bowl.getTotalRuns() == 0) {
						segment.setNumberOfDotBowls(1);
					}
					// this need to be change with the extra type
					segment.setNumberOfBowls(1);
					compressSet.add(segment);
				}
			}
		}
		System.out.println("compress set: " + compressSet.size());
		return compressSet;
	}

	public static Double getMeanRunsInSegment(
			ArrayList<BattingSegment> segmentList) {

		if (segmentList.size()==0)return Double.valueOf(0);

		Double meanRunsInASegment = 0.0;
		Double avgMeanRunsInASegment = 0.0;
		for (BattingSegment segment : segmentList) {
			int totalRunsInSegment = segment.getNumberOfRunsScoredInSegment();
			int totalBowlsFaceInASegment = segment.getNumberOfBowls();
			meanRunsInASegment = meanRunsInASegment
					+ (totalRunsInSegment / totalBowlsFaceInASegment);
		}
		avgMeanRunsInASegment = meanRunsInASegment / segmentList.size();

		return avgMeanRunsInASegment;
	}

	public static Double getAvgRunsInSegment(
			ArrayList<BattingSegment> segmentList) {

		if (segmentList.size()==0)return Double.valueOf(0);

		Double averageRunsInBattingSegment = 0.0;
		int totalRunsInSegment = 0;
		for (BattingSegment segment : segmentList) {
			totalRunsInSegment = totalRunsInSegment
					+ segment.getNumberOfRunsScoredInSegment();
		}

		averageRunsInBattingSegment = totalRunsInSegment
				/ (segmentList.size() + 0.0);

		return averageRunsInBattingSegment;
	}

	public static Double getAvgPressureFactor(
			ArrayList<BattingSegment> segmentList) {

		if (segmentList.size()==0)return Double.valueOf(0);

		Double sumofPressreFactor = 0.0;
		Double avgMeanRunsInASegment = 0.0;
		for (BattingSegment segment : segmentList) {
			int totalRunsInSegment = segment.getNumberOfRunsScoredInSegment();
			int totalDotFaceInASegment = segment.getNumberOfDotBowls();
			sumofPressreFactor = sumofPressreFactor + totalRunsInSegment
					/ (totalDotFaceInASegment + 1);
		}
		avgMeanRunsInASegment = sumofPressreFactor / segmentList.size();

		return avgMeanRunsInASegment;
	}

	public static Double getDotBowlPresentage(
			Map<Integer, com.csa.entity.Bowl> deliveries) {
		int numberOfDotBowls = 0;
		int count=0;
		Double dotBowlPresentage = 0.0;
		for (int i = 1; i <= deliveries.size(); i++) {
			if (deliveries.get(i)!=null) {
				count++;
				Bowl bowl = deliveries.get(i);
				if (bowl.getTotalRuns() == 0) {
					numberOfDotBowls++;
				}
			}
		}
		if (count==0)return 0.0;
		dotBowlPresentage = numberOfDotBowls * (100.0) / count;
		return dotBowlPresentage;
	}

	public static int getNumberOfdots(
			Map<Integer, com.csa.entity.Bowl> deliveries) {
		int numberOfDotBowls = 0;
		for (int i = 1; i <= deliveries.size(); i++) {
			if (deliveries.get(i)!=null) {
				Bowl bowl = deliveries.get(i);
				if (bowl.getTotalRuns() == 0) {
					numberOfDotBowls++;
				}
			}
		}
		return numberOfDotBowls;
	}

	public static Double getInningsSixHittingFrequency(
			Map<Integer, com.csa.entity.Bowl> deliveries) {
		int numberOfsixes = 0;
		int count=0;
		Double sixesPresentage = 0.0;
		for (int i = 1; i <= deliveries.size(); i++) {
			if (deliveries.get(i)!=null) {
				count++;
				Bowl bowl = deliveries.get(i);
				if (bowl.getRuns() == 6) {
					numberOfsixes++;
				}
			}
		}
		if (count==0)return 0.0;
		sixesPresentage = numberOfsixes * (100.0) / (count + 0.0);
		return sixesPresentage;
	}

	public static Double getInningsFourHittingFrequency(
			Map<Integer, com.csa.entity.Bowl> deliveries) {
		int numberOfFours = 0;
		int count=0;
		Double foursPresentage = 0.0;
		for (int i = 1; i <= deliveries.size(); i++) {
			if (deliveries.get(i)!=null) {
				count++;
				Bowl bowl = deliveries.get(i);
				if (bowl.getRuns() == 4) {
					numberOfFours++;
				}
			}
		}
		if (count==0)return 0.0;
		foursPresentage = numberOfFours * (100.0) / (count + 0.0);
		return foursPresentage;
	}

	public static Double getBoundrayRunsPresentage(
			Map<Integer, com.csa.entity.Bowl> deliveries, int totalNumberOfRuns) {

		if (totalNumberOfRuns==0)return 0.0;
		int runsFromFours = 0;
		int runsFromSixes = 0;
		Double boundryRunsPresentage = 0.0;
		for (int i = 1; i <= deliveries.size(); i++) {
			if (deliveries.get(i)!=null) {
				Bowl bowl = deliveries.get(i);
				if (bowl.getRuns() == 4) {
					runsFromFours = runsFromFours + 4;
				} else if (bowl.getRuns() == 4) {
					runsFromSixes = runsFromSixes + 6;
				}
			}
		}
		boundryRunsPresentage = ((runsFromSixes + runsFromFours) * (100.0))
				/ totalNumberOfRuns;
		return boundryRunsPresentage;
	}

	public static Double getDotBowlToRunsRatio(int numberOfDots,
			int numberOfRuns) {

		System.out.println("dots: " + numberOfDots);
		System.out.println("runs: " + numberOfRuns);
		if (numberOfRuns==0) return Double.valueOf(0);
		Double dotBowlToRunsRatio = numberOfDots / (numberOfRuns + 0.0);
		return dotBowlToRunsRatio;
	}
}
