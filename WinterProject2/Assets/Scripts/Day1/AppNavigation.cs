using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;
using UnityEngine.UI;

public class AppNavigation : MonoBehaviour
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

    public void GoToMessages() {
        //SceneManager.LoadScene("EmptyMessages");
        buttonSound();
        Initiate.Fade("EmptyMessages", Color.white, 3.0f);
        FadeAll();
    }

    public void GoToDayOne() {
        buttonSound();
        Initiate.Fade("Day1", Color.white, 4.0f);
    }

    public void GoToShop() {
        buttonSound();
        Initiate.Fade("NonLoadingShop", Color.white, 3.0f);
        FadeAll();
    }

    private void FadeAll() {
        foreach(Button b in buttons) {
            GameObject temp = GameObject.Find(b.name);
            temp.SetActive(false);
        }
    }

    private void buttonSound()
    {
        soundEffect.Play();
    }


}
