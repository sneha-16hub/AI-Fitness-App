import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { getActivityDetail } from '../services/api';
import { Typography, Box, Card, CardContent, Divider } from '@mui/material';

const ActivityDetail = () => {
  const { id } = useParams();
  const [activity, setActivity] = useState(null);

  useEffect(() => {
    const fetchActivityDetail = async () => {
      try {
        const response = await getActivityDetail(id);
        setActivity(response.data);
      } catch (err) {
        console.error("Error fetching activity detail:", err);
      }
    };
    fetchActivityDetail();
  }, [id]);

  if (!activity) {
    return <Typography>Loading...</Typography>;
  }

  return (
    <Box sx={{ maxWidth: 800, mx: 'auto', p: 2 }}>
      <Card sx={{ mb: 2 }}>
        <CardContent>
          <Typography variant="h5" gutterBottom>
            Activity Detail
          </Typography>
          <Typography>Type: {activity.type || activity.activityType}</Typography>
          <Typography>Duration: {activity.duration} minutes</Typography>
          <Typography>Calories Burned: {activity.caloriesBurned || activity.calories}</Typography>
          {activity.date && (
            <Typography>Date: {new Date(activity.date).toLocaleDateString()}</Typography>
          )}
        </CardContent>
      </Card>

      {activity.recommendation && (
        <Card>
          <CardContent>
            <Typography variant="h5" gutterBottom>
              AI Recommendation
            </Typography>
            <Typography variant="h6">Analysis</Typography>
            <Typography paragraph>{activity.recommendation}</Typography>
            <Divider sx={{ my: 2 }} />
            <Typography variant="h6">Improvements</Typography>
            {activity.improvements && Array.isArray(activity.improvements) ? (
              activity.improvements.map((improvement, index) => (
                <Typography key={index} paragraph>
                  - {improvement}
                </Typography>
              ))
            ) : (
              <Typography paragraph>No improvements listed.</Typography>
            )}
             <Typography variant="h6">Suggestions</Typography>
            {activity.suggestions && Array.isArray(activity.suggestions) ? (
              activity.suggestions.map((suggestion, index) => (
                <Typography key={index} paragraph>
                  - {suggestion}
                </Typography>
              ))
            ) : (
              <Typography paragraph>No suggestions listed.</Typography>
            )}
            <Typography variant="h6">Safety Guidelines</Typography>
            {activity.safety && Array.isArray(activity.safety) ? (
              activity.safety.map((safe, index) => (
                <Typography key={index} paragraph>
                  - {safe}
                </Typography>
              ))
            ) : (
              <Typography paragraph>No safety guidelines listed.</Typography>
            )}
            
          </CardContent>
        </Card>
      )}
    </Box>
  );
};

export default ActivityDetail;