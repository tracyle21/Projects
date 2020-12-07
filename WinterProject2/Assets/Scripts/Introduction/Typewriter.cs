using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using UnityEngine.SceneManagement;

public class Typewriter : MonoBehaviour
{
    public float delay = 0.1f;
    public string fullText;
    private string currentText = "";
    public AudioSource typeSound;
    public Button startButton;
    public AudioSource bgMusic;
    // Start is called before the first frame update
    void Start()
    {
        StartCoroutine(PlayIntroduction());
        startButton.gameObject.SetActive(false);
    }

    // Update is called once per frame
    void Update()
    {
        
    }

    public void GoToMenu()
    {
        Debug.Log("Start Scene Transition");
        StartCoroutine(FadeMusic());


    }

    public void TestMenu()
    {
        Initiate.Fade("Menu", Color.white, 3.0f);
    }

    IEnumerator PlayIntroduction()
    {

        string part1 = "You are a student in your senior year of university, and you have come home to your childhood room to clean up before you leave" +
        	" for your first job..";
        string part2 = "You come across an old smartphone in a tattered box... When did you have a phone like this?";
        string part3 = "You try turning it on, and it surprisingly works!";
        string part4 = "What memories and secrets are on this old phone?";
        string[] intro = { part1, part2, part3, part4 };

        for(int i = 0; i < intro.Length; i++)
        {
            typeSound.Play(); //sound is 7 secs long
            for (int j = 0; j <= intro[i].Length; j++)
            {
                currentText = intro[i].Substring(0, j);
                this.GetComponent<Text>().text = currentText;
                yield return new WaitForSeconds(delay);
            }
            typeSound.Pause();
            yield return new WaitForSeconds(2f);
        }
        yield return new WaitForSeconds(1f);
        startButton.gameObject.SetActive(true);
        PlayerPrefs.SetInt("DayProgress", 0);
        PlayerPrefs.Save();
        Debug.Log(PlayerPrefs.GetInt("DayProgress"));
    }

    private IEnumerator FadeMusic()
    {
        float startVolume = bgMusic.volume;

        while (bgMusic.volume > 0)
        {
            bgMusic.volume -= startVolume * Time.deltaTime / 2f;
            yield return null;
        }

        bgMusic.Stop();
        bgMusic.volume = startVolume;

        Initiate.Fade("Menu", Color.white, 3.0f);
        startButton.gameObject.SetActive(false);
        Debug.Log("End Scene Transition");
    }



    //IEnumerator ShowText()
    //{
    //    typeSound.Play(); //sound is 7 secs long
    //    for (int i = 0; i < fullText.Length; i++)
    //    {
    //        currentText = fullText.Substring(0, i);
    //        this.GetComponent<Text>().text = currentText;
    //        yield return new WaitForSeconds(delay);
    //    }
    //    typeSound.Pause();

    //}


}
