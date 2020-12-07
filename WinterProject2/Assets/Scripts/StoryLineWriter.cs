using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using UnityEngine.SceneManagement;

public class StoryLineWriter : MonoBehaviour
{
    public float delay = 0.1f;
    private string currentText = "";
    public AudioSource typeSound;
    private Image bgBox;
    // Start is called before the first frame update
    void Start()
    {
        bgBox = gameObject.GetComponentInParent<Image>();
        bgBox.transform.localPosition = new Vector3(0, 2500, 0);
        if(PlayerPrefs.GetInt("DayProgress") != 1)
        {
            StartCoroutine(PlayStory());
        }

       
    }

    // Update is called once per frame
    void Update()
    {
        
    }

    IEnumerator PlayStory()
    {
        typeSound.Pause();
        yield return new WaitForSeconds(1f);
        bgBox.transform.localPosition = new Vector3(0, 0, 0);
        string part1 = "This is the introduction to Day 1";
        string part2 = "Nothing super exciting happens here";
        string part3 = "This is for the user to get familiar with their phone screen";
        string[] story = { part1, part2, part3 };

        for (int i = 0; i < story.Length; i++)
        {
            typeSound.Play(); //sound is 7 secs long
            for (int j = 0; j <= story[i].Length; j++)
            {
                currentText = story[i].Substring(0, j);
                this.GetComponent<Text>().text = currentText;
                yield return new WaitForSeconds(delay);
            }
            typeSound.Pause();
            yield return new WaitForSeconds(2f);
        }
        yield return new WaitForSeconds(1f);
        bgBox.transform.localPosition = new Vector3(1125, 0, 0);
        PlayerPrefs.SetInt("DayProgress", 1);
        PlayerPrefs.Save();
    }


}
