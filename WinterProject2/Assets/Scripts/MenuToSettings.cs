using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;
using UnityEngine.UI;

public class MenuToSettings : MonoBehaviour
{
    private Button[] buttons;
    public AudioSource soundEffect;

    // Start is called before the first frame update
    void Start()
    {
        buttons = FindObjectsOfType<Button>();
    }

    // Update is called once per frame
    void Update()
    {
        
    }

    public void GoToSettingsScene() {
        //SceneManager.LoadScene("Settings");
        buttonSound();
        Initiate.Fade("Settings", Color.white, 5.0f);
        FadeAll();
    }

    public void GoToMenuScene() {
        buttonSound();
        Initiate.Fade("Menu", Color.white, 2.0f);
        FadeAll();
    }

    public void GoToDayOne() {
        //SceneManager.LoadScene("Day1");
        buttonSound();
        Initiate.Fade("Day1", Color.white, 2.0f);
        FadeAll();
    }

    private void FadeAll()
    {
        foreach (Button b in buttons)
        {
            GameObject temp = GameObject.Find(b.name);
            temp.SetActive(false);
        }
    }

    private void buttonSound()
    {
        soundEffect.Play();
    }
}
