using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using System;

public class Date : MonoBehaviour
{
    private Text textDate;
    // Start is called before the first frame update
    void Awake()
    {
        textDate = GetComponent<Text>();
    }

    // Update is called once per frame
    void Update()
    {
        DateTime curr = DateTime.Now;
        string day = curr.Day.ToString();
        string month = curr.Month.ToString();
        string dayOfWeek = curr.DayOfWeek.ToString();

        string textMonth = "";

        switch(month) {
            case "1":
                textMonth = "January";
                break;
            case "2":
                textMonth = "February";
                break;
            case "3":
                textMonth = "March";
                break;
            case "4": 
                textMonth = "April";
                break;
            case "5":
                textMonth = "May";
                break;
            case "6":
                textMonth = "June";
                break;
            case "7": 
                textMonth = "July";
                break;
            case "8":
                textMonth = "August";
                break;
            case "9":
                textMonth = "September";
                break;
            case "10":
                textMonth = "October";
                break;
            case "11":
                textMonth = "November";
                break;
            case "12":
                textMonth = "December";
                break;
        }       
        textDate.text = dayOfWeek + ", " + textMonth + " " + day;
    }
}
