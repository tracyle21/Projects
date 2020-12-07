using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class MenuClick : MonoBehaviour
{
    public Image image;
    public Button button;
    public Text energy;
    public Image energyIcon;
    public Button nextDay;
    public AudioSource soundEffect;
    // Start is called before the first frame update
    void Start()
    {
        image.enabled = false;
        energy.enabled = false;
        energyIcon.enabled = false;
        nextDay.gameObject.SetActive(false);
    }

    // Update is called once per frame
    void Update()
    {
        
    }

    public void click() {
        image.enabled = !image.enabled;
        energy.enabled = !energy.enabled;
        energyIcon.enabled = !energyIcon.enabled;

        if(image.enabled)
        {
            buttonClick();
            button.image.transform.localScale = new Vector2(-1f, 1f);
            nextDay.gameObject.SetActive(true);
        }
        else
        {
            buttonClick();
            button.image.transform.localScale = new Vector2(1f, 1f);
            nextDay.gameObject.SetActive(false);
        }

    }

    private void buttonClick()
    {
        soundEffect.Play();
    }
}
