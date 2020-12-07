﻿using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using System;

public class Clock : MonoBehaviour
{
    private Text textClock;
    // Start is called before the first frame update
    void Awake()
    {
        textClock = GetComponent<Text>();
    }

    // Update is called once per frame
    void Update()
    {
        DateTime time = DateTime.Now;
        string hour = LeadingZero(time.Hour);
        string minute = LeadingZero(time.Minute);
        textClock.text = hour + ":" + minute;
    }

    string LeadingZero(int n) {
        return n.ToString().PadLeft(2, '0');
    }
}
