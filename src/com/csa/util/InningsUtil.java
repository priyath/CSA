package com.csa.util;

import java.util.ArrayList;
import java.util.Map;
import com.csa.entity.Bowl;
import com.csa.entity.MatchDetails;
import com.csa.visualization.BattingSegment;
import com.csa.visualization.InningByInningsResults;

public class InningsUtil {

	public static InningByInningsResults generateInningsByInningsResultsFirstInnings(
			MatchDetails match) {

		InningByInningsResults inningsFactorSet = new InningByInningsResults();

		// inningsId will be autoGenerated

		inningsFactorSet.setMatchId(match.getMatchId());
		inningsFactorSet.setFirstInningsOrSecondInnings(1);

		ArrayList<BattingSegment> segmentList = getBattingSegmentList(match
				.getFirstInnings().getDeliveries());

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
		inningsFactorSet.setDotBowlPrerentage(getDotBowlPresentage(match
				.getFirstInnings().getDeliveries()));

		// number of Wickets lost in first Innings
		inningsFactorSet.setNumberOfWicketsLost(match.getFirstInnings()
				.getNumberOfWickets());

		// sixHittingFrequency
		Double sixHittingFrequency = getInningsSixHittingFrequency(match
				.getFirstInnings().getDeliveries());
		inningsFactorSet.setSixHittingFrequency(sixHittingFrequency);

		// fourHittingFrequency
		Double fourHittingFrequency = getInningsFourHittingFrequency(match
				.getFirstInnings().getDeliveries());
		inningsFactorSet.setFourHittingFrequency(fourHittingFrequency);

		// Boundary Percentage
		Double boundrayPresentage = getBoundrayRunsPresentage(match
				.getFirstInnings().getDeliveries(), match.getFirstInnings()
				.getNumberOfRunsScored());
		inningsFactorSet.setBoundaryRunsPresentage(boundrayPresentage);

		// DotBawlToRunsRatio
		int numberOfDots = getNumberOfdots(match.getFirstInnings()
				.getDeliveries());
		int inningsScore = match.getFirstInnings().getNumberOfRunsScored();

		Double dotBowlToRunsRatio = getDotBowlToRunsRatio(numberOfDots,
				inningsScore);

		inningsFactorSet.setDotBowlToRunsRatio(dotBowlToRunsRatio);

		// for first innings
		int side = match.getResult().getWonByFirstBatOrSecondBat();
		// team 1 wins
		if (side == 1) {
			inningsFactorSet.setWinOrLoss("win");
		} else if (side == 2) {
			inningsFactorSet.setWinOrLoss("loss");
		} else {
			inningsFactorSet.setWinOrLoss("draw");
		}
		return inningsFactorSet;
	}

	public static InningByInningsResults generateInningsByInningsResultsSecondInnings(
			MatchDetails match) {

		InningByInningsResults inningsFactorSet = new InningByInningsResults();

		// inningsId will be autoGenerated

		inningsFactorSet.setMatchId(match.getMatchId());
		inningsFactorSet.setFirstInningsOrSecondInnings(2);

		/********************************* segment list **********************************/

		ArrayList<BattingSegment> segmentList = getBattingSegmentList(match
				.getSecondInnings().getDeliveries());

		// setNumber of segments
		inningsFactorSet.setNumberOfBattingSegments(segmentList.size());

		// set avgMean runs in batting segment
		inningsFactorSet
				.setAvgMeanRunsInBattingSegment(getMeanRunsInSegment(segmentList));

		// set avg runs in segment
		inningsFactorSet
				.setAvgRunsInBattingSegment(getAvgRunsInSegment(segmentList));

		// set avg pressure factor
		inningsFactorSet
				.setAvgPressureFactor(getAvgPressureFactor(segmentList));

		/********************************* segment list **********************************/

		// dot bowl Percentage
		inningsFactorSet.setDotBowlPrerentage(getDotBowlPresentage(match
				.getSecondInnings().getDeliveries()));

		// numberOfWickets lost
		inningsFactorSet.setNumberOfWicketsLost(match.getSecondInnings()
				.getNumberOfWickets());

		// sixHittingFrequency
		Double sixHittingFrequency = getInningsSixHittingFrequency(match
				.getSecondInnings().getDeliveries());
		inningsFactorSet.setSixHittingFrequency(sixHittingFrequency);

		// fourHittingFrequency
		Double fourHittingFrequency = getInningsFourHittingFrequency(match
				.getSecondInnings().getDeliveries());
		inningsFactorSet.setFourHittingFrequency(fourHittingFrequency);

		// Boundary Percentage
		Double boundrayPresentage = getBoundrayRunsPresentage(match
				.getSecondInnings().getDeliveries(), match.getSecondInnings()
				.getNumberOfRunsScored());
		inningsFactorSet.setBoundaryRunsPresentage(boundrayPresentage);

		// DotBawlToRunsRatio
		int numberOfDots = getNumberOfdots(match.getSecondInnings()
				.getDeliveries());
		int inningsScore = match.getSecondInnings().getNumberOfRunsScored();

		// dot bowl To runs Ratio
		Double dotBowlToRunsRatio = getDotBowlToRunsRatio(numberOfDots,
				inningsScore);
		inningsFactorSet.setDotBowlToRunsRatio(dotBowlToRunsRatio);

		// set the win or loose
		int side = match.getResult().getWonByFirstBatOrSecondBat();

		// team 1 wins
		if (side == 2) {
			inningsFactorSet.setWinOrLoss("win");
		} else if (side == 1) {
			inningsFactorSet.setWinOrLoss("loss");
		} else {
			inningsFactorSet.setWinOrLoss("draw");
		}
		return inningsFactorSet;
	}

	public static ArrayList<BattingSegment> getBattingSegmentList(
			Map<Integer, com.csa.entity.Bowl> deliveries) {

		ArrayList<BattingSegment> compressSet = new ArrayList<>();
		BattingSegment lastElement;
		for (int i = 1; i <= deliveries.size(); i++) {
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
		return compressSet;
	}

	public static Double getMeanRunsInSegment(
			ArrayList<BattingSegment> segmentList) {

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
		Double dotBowlPresentage = 0.0;
		for (int i = 1; i <= deliveries.size(); i++) {
			Bowl bowl = deliveries.get(i);
			if (bowl.getTotalRuns() == 0) {
				numberOfDotBowls++;
			}
		}
		dotBowlPresentage = numberOfDotBowls * (100.0) / deliveries.size();
		return dotBowlPresentage;
	}

	public static int getNumberOfdots(
			Map<Integer, com.csa.entity.Bowl> deliveries) {
		int numberOfDotBowls = 0;
		for (int i = 1; i <= deliveries.size(); i++) {
			Bowl bowl = deliveries.get(i);
			if (bowl.getTotalRuns() == 0) {
				numberOfDotBowls++;
			}
		}
		return numberOfDotBowls;
	}

	public static Double getInningsSixHittingFrequency(
			Map<Integer, com.csa.entity.Bowl> deliveries) {
		int numberOfsixes = 0;
		Double sixesPresentage = 0.0;
		for (int i = 1; i <= deliveries.size(); i++) {
			Bowl bowl = deliveries.get(i);
			if (bowl.getRuns() == 6) {
				numberOfsixes++;
			}
		}
		sixesPresentage = numberOfsixes * (100.0) / (deliveries.size() + 0.0);
		return sixesPresentage;
	}

	public static Double getInningsFourHittingFrequency(
			Map<Integer, com.csa.entity.Bowl> deliveries) {
		int numberOfFours = 0;
		Double foursPresentage = 0.0;
		for (int i = 1; i <= deliveries.size(); i++) {
			Bowl bowl = deliveries.get(i);
			if (bowl.getRuns() == 4) {
				numberOfFours++;
			}
		}
		foursPresentage = numberOfFours * (100.0) / (deliveries.size() + 0.0);
		return foursPresentage;
	}

	public static Double getBoundrayRunsPresentage(
			Map<Integer, com.csa.entity.Bowl> deliveries, int totalNumberOfRuns) {
		int runsFromFours = 0;
		int runsFromSixes = 0;
		Double boundryRunsPresentage = 0.0;
		for (int i = 1; i <= deliveries.size(); i++) {
			Bowl bowl = deliveries.get(i);
			if (bowl.getRuns() == 4) {
				runsFromFours = runsFromFours + 4;
			} else if (bowl.getRuns() == 4) {
				runsFromSixes = runsFromSixes + 6;
			}
		}
		boundryRunsPresentage = ((runsFromSixes + runsFromFours) * (100.0))
				/ totalNumberOfRuns;
		return boundryRunsPresentage;
	}

	public static Double getDotBowlToRunsRatio(int numberOfDots,
			int numberOfRuns) {
		Double dotBowlToRunsRatio = numberOfDots / (numberOfRuns + 0.0);
		return dotBowlToRunsRatio;
	}
}
