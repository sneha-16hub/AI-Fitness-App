import React from 'react'
import { Box, FormControl, InputLabel, MenuItem, TextField } from '@mui/material';
import { useState } from 'react';
import Select from '@mui/material/Select';
import {addActivity} from '../services/api';
import Button from '@mui/material/Button';

const ActivityForm = ({onActivityAdded}) => {

  const [activity,setActivity]= useState({
    activityType: "Running", duration:'',caloriesBurned:'',
    additionalMetrics:{}
  })

  const handleSubmit = async (e) =>{
    e.preventDefault();
    try{
      await addActivity(activity);
      onActivityAdded();
      setActivity({activityType: "Running", duration:'',caloriesBurned:''});
    }catch(err){
      console.error("Error adding activity:", err);
    }
  }
  return (
     <Box component="form" onSubmit={handleSubmit} sx={{mb:4}}>
        <FormControl fullWidth sx={{mb:2}}>
          <InputLabel>Activity Type</InputLabel>
          <Select
            value={activity.activityType}
            
            onChange={(e) => setActivity({...activity, activityType: e.target.value})}>
              <MenuItem value="Running">Running</MenuItem>
              <MenuItem value="Walking">Walking</MenuItem>
              <MenuItem value="Cycling">Cycling</MenuItem>
              
          </Select>
        </FormControl>
        <TextField fullWidth
                    label="Duration"
                    type='number'
                    sx={{mb:2}}
                    value={activity.duration}
                    onChange={(e)=> setActivity({...activity,duration: e.target.value})}/>
        <TextField fullWidth
                    label="Calories Burned"
                    type='number'
                    sx={{mb:2}}
                    value={activity.caloriesBurned}
                    onChange={(e)=> setActivity({...activity,caloriesBurned: e.target.value})}/>
        <Button type="submit" variant='contained'>Add Activity</Button>
      
      </Box>
  )
}

export default ActivityForm