using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using System;

public class KeepMusic : MonoBehaviour
{
    public AudioClip afternoon;
    public AudioClip earlyMorning;
    public AudioClip morning;
    public AudioClip night;
    // Start is called before the first frame update
    void Start()
    {
    }

    // Update is called once per frame
    void Update()
    {
       
    }

    void Awake() {
        DateTime time = DateTime.Now;
        int hour = time.Hour;

        GameObject[] objs = GameObject.FindGameObjectsWithTag("Music");
        if(objs.Length > 1) {
            Destroy(this.gameObject);
        }

        if(hour >= 0 && hour <= 7)
        {
            this.gameObject.GetComponent<AudioSource>().clip = earlyMorning;
            this.gameObject.GetComponent<AudioSource>().Play();
            this.gameObject.GetComponent<AudioSource>().loop = true;
        } else if(hour >= 8 && hour <= 12)
        {
            this.gameObject.GetComponent<AudioSource>().clip = morning;
            this.gameObject.GetComponent<AudioSource>().Play();
            this.gameObject.GetComponent<AudioSource>().loop = true;
        }
        else if (hour >= 13 && hour <= 18)
        {
            this.gameObject.GetComponent<AudioSource>().clip = afternoon;
            this.gameObject.GetComponent<AudioSource>().Play();
            this.gameObject.GetComponent<AudioSource>().loop = true;
        }
        else
        {
            this.gameObject.GetComponent<AudioSource>().clip = night;
            this.gameObject.GetComponent<AudioSource>().Play();
            this.gameObject.GetComponent<AudioSource>().loop = true;
        }
        DontDestroyOnLoad(this.gameObject);
    }

}
